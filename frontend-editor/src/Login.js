import React, {Component} from "react";

export default class Login extends Component {

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
        <input type="text" id="inputUsername" className="form-control" placeholder="Username" required=""  autoFocus="" onChange={this.setUsername} value={this.state.username} onKeyUp={this.handleKeyPress} />
        <label htmlFor="inputPassword" className="sr-only">Password</label>
        <input type="password" id="inputPassword" className="form-control" placeholder="Password" required="" onChange={this.setPassword} value={this.state.password} onKeyUp={this.handleKeyPress} />
        <button className="btn btn-lg btn-primary btn-block" type="button" onClick={() => this.props.onLogin(this.state.username, this.state.password)}>Sign in</button>
        { this.props.errorMessage && <div>{this.props.errorMessage}</div>}
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

  handleKeyPress = (e) => {
    if (e.key === "Enter") {
      this.props.onLogin(this.state.username, this.state.password)
    }
  }

}
