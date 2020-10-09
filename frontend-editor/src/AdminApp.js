import React, {Component} from "react";

import AdminIndex from "AdminIndex";
import {fetchMe, login, logout} from "../../frontend-shared/api";

import "admin.scss";

class AdminLogin extends Component {

  constructor(props) {
    super(props);

    this.state = {
      username: "",
      password: ""
    }
  }

  render() {
    return (
      <form className="form-signin text-center">
        <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>
        <label htmlFor="inputUsername" className="sr-only">Username</label>
        <input type="text" id="inputUsername" className="form-control" placeholder="Username" required=""  autoFocus="" onChange={this.setUsername} />
        <label htmlFor="inputPassword" className="sr-only">Password</label>
        <input type="password" id="inputPassword" className="form-control" placeholder="Password" required="" onChange={this.setPassword} />
        <button className="btn btn-lg btn-primary btn-block" type="button" onClick={() => this.props.onLogin(this.state.username, this.state.password)}>Sign in</button>
      </form>
    );
  }

  setUsername = (e) => {
    this.setState({
      username: e.target.value
    });
  }

  setPassword = (e) => {
    this.setState({
      password: e.target.value
    });
  }

}

export default class AdminApp extends Component {
  state = {
    user: null,
    loading: true
  };

  constructor(props) {
    super(props);

    this.loadUser();
  }

  render() {
    return (
      <div>
        {!this.state.loading && this.state.user && <AdminIndex onLogout={this.logout} />}
        {!this.state.loading && !this.state.user && <AdminLogin onLogin={this.login} />}
      </div>
    );
  }

  loadUser() {
    fetchMe().then(user => {
      this.setState({
        user,
        loading: false
      });
    }, error => {
      this.setState({
        user: null,
        loading: false
      });
    });
  }

  login = (username, password) => {
    if (username !== "" && password !== "") {
      login(username, password).then(
        r => {
          this.loadUser();
        }
      );
    }
  }

  logout = () => {
    logout().then(() => {
      this.loadUser();
    });
  }


}
