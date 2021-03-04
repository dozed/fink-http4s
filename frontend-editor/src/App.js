import React, {Component} from "react";
import { createBrowserHistory } from "history";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import Navbar from "react-bootstrap/Navbar";
import { LinkContainer } from "react-router-bootstrap";
import Nav from "react-bootstrap/Nav";
import Container from "react-bootstrap/Container";

import Galleries from "Galleries";
import CreateGallery from "CreateGallery";
import EditGallery from "EditGallery";
import Posts from "Posts";
import CreatePost from "CreatePost";
import EditPost from "EditPost";
import Pages from "Pages";
import CreatePage from "CreatePage";
import EditPage from "EditPage";
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
        {!this.state.loading && !this.state.user && <Login onLogin={this.login} errorMessage={this.state.loginErrorMessage} />}
        {!this.state.loading && this.state.user &&
          <Router>
            <Navbar bg="light" expand="lg">
              <LinkContainer to="/">
                <Navbar.Brand>fink</Navbar.Brand>
              </LinkContainer>
              <Navbar.Toggle aria-controls="basic-navbar-nav"/>
              <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="mr-auto">
                  <LinkContainer to="/posts">
                    <Nav.Link>Posts</Nav.Link>
                  </LinkContainer>
                  <LinkContainer to="/pages">
                    <Nav.Link>Pages</Nav.Link>
                  </LinkContainer>
                  <LinkContainer to="/galleries">
                    <Nav.Link>Galleries</Nav.Link>
                  </LinkContainer>
                </Nav>
              </Navbar.Collapse>
              <Navbar.Brand className="pull-right">
                <button className="btn btn-primary" onClick={() => this.logout()}>Logout</button>
              </Navbar.Brand>
            </Navbar>
            <Container fluid={true}>
              <Switch>
                {/*<Route path="/" exact component={Home} />*/}
                <Route path="/galleries" exact component={Galleries} />
                <Route path="/galleries/create" exact component={CreateGallery} />
                <Route path="/galleries/:galleryId" component={EditGallery} />
                <Route path="/posts" exact component={Posts} />
                <Route path="/posts/create" exact component={CreatePost} />
                <Route path="/posts/:postId" component={EditPost} />
                <Route path="/pages" exact component={Pages} />
                <Route path="/pages/create" exact component={CreatePage} />
                <Route path="/pages/:pageId" component={EditPage} />
              </Switch>
            </Container>
          </Router>
        }
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
