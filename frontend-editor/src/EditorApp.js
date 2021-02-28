import React, {Component} from "react";

import EditorIndex from "EditorIndex";
import EditorLogin from "EditorLogin";
import {fetchMe, login, logout} from "../../frontend-shared/api";

import "editor.scss";

export default class EditorApp extends Component {
  state = {
    user: null,
    loading: true,
    errorMessage: null
  };

  constructor(props) {
    super(props);

    this.loadUser();
  }

  render() {
    return (
      <div>
        {!this.state.loading && this.state.user && <EditorIndex onLogout={this.logout} />}
        {!this.state.loading && !this.state.user && <EditorLogin onLogin={this.login} errorMessage={this.state.errorMessage} />}
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
    if (username !== "" && password !== "") {
      login(username, password).then(
        res => this.loadUser()
      ).catch(err => {
        if (err.status === 403) {
          this.setState({ errorMessage: err.response.body.message });
        } else {
          this.setState({ errorMessage: "There was an error while logging in." });
        }
      });
    }
  }

  logout = () => {
    logout().then(() => {
      this.loadUser();
    });
  }


}
