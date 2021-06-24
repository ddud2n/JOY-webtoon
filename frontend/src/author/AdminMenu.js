import React, { Component } from 'react';
import { Button } from 'antd';
import {Link, withRouter} from "react-router-dom";
import './AdminMenu.css';
import {getCurrentUser} from "../util/APIUtils";

class AuthorMenu extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: null
        }
    }

    componentDidMount() {
        getCurrentUser()
            .then(response => {
                this.setState({
                    user: response
                }, function(){
                    console.log(response);
                });
            }).catch(error => {
            console.log(error)
        });
    }


    render() {

        return (
            this.state.user ? (
            <div className="admin-menu-container">
                <h2> {this.state.user.name} 작가님, 환영합니다</h2>
                <Button type="primary" block size="large">
                    <Link to="/newAddAuthor">
                        새 웹툰 등록
                    </Link>
                </Button>
                <Button type="primary" block size="large">
                    <Link to="/newEpiAuthor">
                    에피소드 업로드
                    </Link>
                </Button>
                <Button type="primary" block size="large">
                    <Link to="/editListAuthor">
                    기존 웹툰 수정
                    </Link>
                </Button>
            </div>
            ): null
        );
    }
}

export default withRouter(AuthorMenu);