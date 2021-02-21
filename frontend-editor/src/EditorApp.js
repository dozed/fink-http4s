import React, {Component} from "react";

import EditorIndex from "EditorIndex";
import EditorLogin from "EditorLogin";
import {fetchMe, login, logout} from "../../frontend-shared/api";

import "editor.scss";

export default class EditorApp extends Component {
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
        {!this.state.loading && this.state.user && <EditorIndex onLogout={this.logout} />}
        {!this.state.loading && !this.state.user && <EditorLogin onLogin={this.login} />}
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
