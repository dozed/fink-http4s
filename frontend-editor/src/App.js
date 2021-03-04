import React, {Component} from "react";
import { createBrowserHistory } from "history";

import EditorIndex from "EditorIndex";
import Login from "Login";
import {fetchMe, login, logout} from "../../frontend-shared/api";

import "editor.scss";

const history = createBrowserHistory();

export default class App extends Component {
  state = {
    user: null,
    loading: true,
    loginErrorMessage: null
  };

  constructor(props) {
    super(props);

    this.loadUser();
  }

  render() {
    return (
      <div>
        {!this.state.loading && this.state.user && <EditorIndex onLogout={this.logout} />}
        {!this.state.loading && !this.state.user && <Login onLogin={this.login} errorMessage={this.state.loginErrorMessage} />}
      </div>
    );
  }

  loadUser() {
    fetchMe().then(res => {
      this.setState({
        user: res.body,
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
    login(username, password).then(
      res => {
        this.setState({ loginErrorMessage: null });
        this.loadUser();
      }
    ).catch(err => {
      if (err.status === 403) {
        this.setState({ loginErrorMessage: err.response.body.message });
      } else {
        this.setState({ loginErrorMessage: "There was an error while logging in." });
      }
    });
  }

  logout = () => {
    logout().then(() => {
      history.push("/");
      this.loadUser();
    });
  }


}
