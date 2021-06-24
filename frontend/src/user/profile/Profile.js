import React, { Component } from 'react';
import NotFound from '../../common/NotFound';
import ServerError from '../../common/ServerError';
import LoadingIndicator  from '../../common/LoadingIndicator';
import {Avatar, Table, Button, Form, Input} from 'antd';
import { getAvatarColor } from '../../util/Colors';
import { getUserProfile, chargeCash, getCashRecord, CheckRentIng } from '../../util/APIUtils';
import {fetchFav, deleteFavById} from '../../util/APIAdmin';
import {Link, withRouter} from "react-router-dom";
import "./Profile.css";

const { TextArea } = Input;



class Profile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: null,
            isLoading: false,
            cash : '',
            favs:[],
            cashrecords:[],
            rentrecords:[],
            res11 : null
        }
        this.loadUserProfile = this.loadUserProfile.bind(this);
        this.loadFav = this.loadFav.bind(this);
        this.loadCashRecord = this.loadCashRecord.bind(this);
        this.loadRentRecord = this.loadRentRecord.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.chargeCash = this.chargeCash.bind(this);
        this.seeWebtoon = this.seeWebtoon.bind(this);
        this.onChoice = this.onChoice.bind(this);
    }


    onDelete(fno){
        deleteFavById(fno)
            .then(res => {
                this.setState({favs:this.state.favs.filter(fav => fav.fno !== fno)})
            })
    }

    /*
[
{"id":
    {"timestamp":1624359542,
    "date":"2021-06-22T19:59:02.000+09:00"},
"userid":"1",
"change_amount":1111,
"content":"CHARGE",
"createdAt":"2021-06-22T19:59:01.964",
"toonname":"",
"epiname":"",
"epino":""}
]
    * */

    onChoice(record){
        console.log(record.content)
        if (record.content === 'RENT_WEBTOON') return '대여';
        else if(record.content === 'CHARGE')return '충전';
        else if(record.content === 'MONTH_REVENUE')return 'NFT수익';
        else return '환불';
    }

    seeWebtoon(epiid){
        window.scrollTo(0, 0);
        this.props.history.push('/viewer/'+epiid)
    }

    loadFav(username){
        fetchFav(username)
            .then(res => {
                this.setState({
                favs : res
                }, function(){
                    console.log(res)
                });
            })
            .catch(error => {
                console.log(error);
            });
    }
/*
[{"createdAt":"2021-06-03T20:57:47Z",
"updatedAt":"2021-06-03T20:57:47Z",
"crno":1,
"changeAmount":10,
"toonNumber":null,
"episodeNumber":null,
"contents":[{"id":2,"name":"CHARGE"}]},

{"createdAt":"2021-06-03T21:50:41Z",
"updatedAt":"2021-06-03T21:50:41Z",
"crno":2,
"changeAmount":12,
"toonNumber":null,
"episodeNumber":null,
"contents":[{"id":2,"name":"CHARGE"}]}]
 */


    loadCashRecord(){
        getCashRecord()
            .then(res => {
                console.log(res)
                this.setState({
                    cashrecords : res
                }, function(){
                    console.log(res)
                });
            })
            .catch(error => {
                console.log(error);
            });
    }


    loadRentRecord(){
        CheckRentIng()
            .then(res => {
                this.setState({
                    rentrecords : res
                }, function(){
                    console.log(JSON.stringify(res))
                });
            })
            .catch(error => {
                console.log(error);
            });

    }


    loadUserProfile(username) {
        this.setState({
            isLoading: true
        });

        getUserProfile(username)
        .then(response => {
            this.setState({
                user: response,
                isLoading: false
            }, function(){
                console.log(response);
            });
        }).catch(error => {
            if(error.status === 404) {
                this.setState({
                    notFound: true,
                    isLoading: false
                });
            } else {
                this.setState({
                    serverError: true,
                    isLoading: false
                });        
            }
        });        
    }


    chargeCash(){
        chargeCash(parseInt(this.state.cash))
    }

    componentDidMount() {
        const username = this.props.match.params.username;
        this.loadUserProfile(username);
        this.loadFav(username);
        this.loadCashRecord();
        this.loadRentRecord();
    }

    componentDidUpdate(nextProps) {
        if(this.props.match.params.username !== nextProps.match.params.username) {
            this.loadUserProfile(nextProps.match.params.username);
        }        
    }

    onChange=(e)=>{
        this.setState({cash:e.target.value});
    }

    render() {

        if(this.state.isLoading) {
            return <LoadingIndicator />;
        }

        if(this.state.notFound) {
            return <NotFound />;
        }

        if(this.state.serverError) {
            return <ServerError />;
        }

        const columns =[
            {
                title: '제목',
              dataIndex: 'title',
              key: 'title',
              render: (text, record) => <Link to={'/webtoon/' + record.webtoonId}>{text}</Link>
            },
            {
                title: 'Action',
                key: 'action',
                className: 'action',
                render: (text, record) => (
                  <span>
                    <Button onClick={()=>this.onDelete(record.fno)}>
                        삭제
                    </Button>
                  </span>
                ),
              }
        ]

        const columns1 =[
            {
                title: '날짜',
                dataIndex: 'createdAt',
                key: 'createdAt'
            },
            {
                title: '금액',
                key: 'change_amount',
                dataIndex: 'change_amount'
            },
            {
                title: '내용',
                key: 'content',
                dataIndex: 'content',

                render: (text, record) => (
                    <span>
                        {this.onChoice(record)}
                    </span>
                )

            },
            {
                title: '웹툰',
                key: 'toonname',
                dataIndex: 'toonname'
            },
            {
                title: '회차',
                key: 'epiname',
                dataIndex:'epiname'
            }
        ]


        const columns2 =[
            {
                title: '날짜',
                dataIndex: 'createdAt',
                key: 'createdAt'
            },
            {
                title: '웹툰',
                key: 'toonNumber',
                dataIndex: 'toonname'
            },
            {
                title: '회차',
                key: 'episodeNumber',
                dataIndex:'epiname'
            },
            {
                title: '',
                key: 'action',
                className: 'action',
                render: (text, record) => (
                    <span>
                    <Button className='seeButton' onClick={() => this.seeWebtoon(record.epino)}>
                        보러가기
                    </Button>
                  </span>
                )
            }
        ]


        return (
            <div className="profile">
                { 
                    this.state.user ? (
                        <div className="profile_container">
                            <div className="user-profile">
                                <div className="user-details">
                                    <div className="cash_charge">
                                        <div><strong>보유캐시 : {this.state.user.cash}  원</strong></div>
                                        <Form onSubmit={this.chargeCash} className="cash_form">
                                            <Form.Item>
                                                <TextArea rows={1} onChange={this.onChange} value={this.state.cash}/>
                                            </Form.Item>
                                            <Form.Item>
                                                <Button type="primary" className="cashButton" htmlType="submit" >
                                                    캐시 충전하기
                                                </Button>
                                            </Form.Item>
                                        </Form>
                                    </div>
                                    <div className="user-avatar">
                                        <Avatar className="user-avatar-circle" style={{ backgroundColor: getAvatarColor(this.state.user.name)}}>
                                            {this.state.user.name[0].toUpperCase()}
                                        </Avatar>
                                    </div>
                                    <div className="user-summary">
                                        <div className="full-name">{this.state.user.name}</div>
                                        <div className="username">@{this.state.user.username}</div>
                                    </div>

                                </div> 
                            </div>

                            <div className="favTable_container">
                                <div className="favTitle_container">
                                    <span className="favTitle">선호 작품 목록</span>
                                </div>
                                <Table dataSource={this.state.favs} columns={columns} pagination={{pageSize:10}}/>
                            </div>

                            <br></br>
                            <br></br>

                            <div className="favTable_container">
                                <div className="favTitle_container">
                                    <span className="favTitle">캐시 내역 목록</span>
                                </div>
                                <Table dataSource={this.state.cashrecords} columns={columns1} pagination={{pageSize:10}}/>
                            </div>

                            <br></br>
                            <br></br>

                            <div className="favTable_container">
                                <div className="favTitle_container">
                                    <span className="favTitle">대여 웹툰 목록</span>
                                </div>
                                <Table dataSource={this.state.rentrecords} columns={columns2} pagination={{pageSize:10}}/>
                            </div>

                        </div>
                    ): null               
                }
            </div>
        );
    }
}

export default withRouter(Profile);