import React, { Component } from 'react';
import {
  Route,
  withRouter,
  Switch
} from 'react-router-dom';

import { getCurrentUser } from './util/APIUtils';
import { ACCESS_TOKEN } from './constants';

import './App.css';
import Main from "./container/Main";
import WebtoonHome from "./container/WebtoonHome";
import Viewer from "./container/Viewer";

import Login from "./user/login/Login";
import Signup from "./user/signup/Signup";
import Profile from './user/profile/Profile';
import AppHeader from './common/AppHeader';
import NotFound from './common/NotFound';
import BanPage from './common/BanPage';
import LoadingIndicator from './common/LoadingIndicator';
import PrivateRoute from './common/PrivateRoute';
import { Layout, notification } from 'antd';

import AdminMenu from './admin/AdminMenu';
import NewAdd from './admin/NewAdd';
import NewEpi from './admin/NewEpi';
import EditToonList from './admin/EditToonList';
import EditEpiList from './admin/EditEpiList';
import EditToon from './admin/EditToon';
import EditEpi from './admin/EditEpi';

//작가
import AuthorMenu from './author/AdminMenu';
import NewEpiAuthor from './author/NewEpi';
import NewAddAuthor from './author/NewAdd';
import EditToonListAuthor from './author/EditToonList';
import EditEpiListAuthor from './author/EditEpiList';
import EditToonAuthor from './author/EditToon';
import EditEpiAuthor from './author/EditEpi';

const { Content } = Layout;

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
          currentUser: null,
          isAuthenticated: false,
          isLoading: false,
          role: null,
          username : null
        }
        this.handleLogout = this.handleLogout.bind(this);
        this.loadCurrentUser = this.loadCurrentUser.bind(this);
        this.handleLogin = this.handleLogin.bind(this);

        notification.config({
            placement: 'topRight',
            top: 70,
            duration: 3,
        });    
    }

    loadCurrentUser() {
        this.setState({
          isLoading: true
        });
        getCurrentUser()
        .then(response => {
          this.setState({
            role : response.authorities[0].authority,
            currentUser: response,
            isAuthenticated: true,
            isLoading: false,
            username : response.username
          }, function(){
            console.log(this.state);
          });
        }).catch(error => {
          this.setState({
            isLoading: false
          });  
        });
      }

      componentDidMount() {
        this.loadCurrentUser();
      }
    
      handleLogout(redirectTo="/", notificationType="success", description="로그아웃 되었습니다.") {
        localStorage.removeItem(ACCESS_TOKEN);
    
        this.setState({
          currentUser: null,
          isAuthenticated: false,
          role:null,
          username: null
        });
    
        this.props.history.push(redirectTo);
        
        notification[notificationType]({
          message: 'JOY Toon',
          description: description,
        });
      }

      handleLogin() {
        notification.success({
          message: 'JOY Toon',
          description: "로그인 되었습니다.",
        });
        this.loadCurrentUser();
        this.props.history.push("/");
      }


    render() {
        if(this.state.isLoading) {
            return <LoadingIndicator />
          }
        return (
            <Layout className="app-container">
                <AppHeader isAuthenticated={this.state.isAuthenticated} 
                    currentUser={this.state.currentUser} 
                    onLogout={this.handleLogout} role={this.state.role}/>

            <Content className="app-content">
                <div className="container">
                  <Switch>
                        <Route exact path="/" component={Main} />
                        {/* ":" 뒤에 있는 것은 prams */}
                        <Route path="/webtoon/:webtoonId" 
                        render={(props) => <WebtoonHome username={this.state.username} {...props} />}></Route>
                        <Route path="/viewer/:episodeId"
                        render={(props) => <Viewer username={this.state.username} {...props} />}></Route>
                        <Route path="/login" 
                        render={(props) => <Login onLogin={this.handleLogin} {...props} />}></Route>
                        <Route path="/signup" component={Signup}></Route>
                        <Route path="/users/:username" 
                        render={(props) => <Profile isAuthenticated={this.state.isAuthenticated} currentUser={this.state.currentUser} {...props}  />}>
                        </Route>
                        <PrivateRoute authenticated={this.state.isAuthenticated} path="/newAdd" component={NewAdd} handleLogout={this.handleLogout}></PrivateRoute>
                        <PrivateRoute authenticated={this.state.isAuthenticated} path="/newEpi" component={NewEpi} handleLogout={this.handleLogout}></PrivateRoute>
                        <PrivateRoute authenticated={this.state.isAuthenticated} path="/adminmenu" component={AdminMenu} handleLogout={this.handleLogout}></PrivateRoute>
                        <PrivateRoute authenticated={this.state.isAuthenticated} path="/editList" component={EditToonList} handleLogout={this.handleLogout}></PrivateRoute>
                        <PrivateRoute authenticated={this.state.isAuthenticated} path="/editToon/:id" component={EditToon} handleLogout={this.handleLogout}></PrivateRoute>
                        <PrivateRoute authenticated={this.state.isAuthenticated} path="/editEpiList/:id" component={EditEpiList} handleLogout={this.handleLogout}></PrivateRoute>
                        <PrivateRoute authenticated={this.state.isAuthenticated} path="/editEpi/:id" component={EditEpi} handleLogout={this.handleLogout}></PrivateRoute>

                      <PrivateRoute authenticated={this.state.isAuthenticated} path="/newAddAuthor" component={NewAddAuthor} handleLogout={this.handleLogout}></PrivateRoute>
                      <PrivateRoute authenticated={this.state.isAuthenticated} path="/newEpiAuthor" component={NewEpiAuthor} handleLogout={this.handleLogout}></PrivateRoute>
                      <PrivateRoute authenticated={this.state.isAuthenticated} path="/authormenu" component={AuthorMenu} handleLogout={this.handleLogout}></PrivateRoute>
                      <PrivateRoute authenticated={this.state.isAuthenticated} path="/editListAuthor" component={EditToonListAuthor} handleLogout={this.handleLogout}></PrivateRoute>
                      <PrivateRoute authenticated={this.state.isAuthenticated} path="/editToonAuthor/:id" component={EditToonAuthor} handleLogout={this.handleLogout}></PrivateRoute>
                      <PrivateRoute authenticated={this.state.isAuthenticated} path="/editEpiListAuthor/:id" component={EditEpiListAuthor} handleLogout={this.handleLogout}></PrivateRoute>
                      <PrivateRoute authenticated={this.state.isAuthenticated} path="/editEpiAuthor/:id" component={EditEpiAuthor} handleLogout={this.handleLogout}></PrivateRoute>


                      <Route component={NotFound}></Route>
                      <Route component={BanPage}></Route>
                  </Switch>
                </div>
            </Content>
        </Layout>);
    }
}
export default withRouter(App);

