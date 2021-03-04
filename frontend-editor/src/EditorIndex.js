import React, {Component} from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import Container from "react-bootstrap/Container";
import {LinkContainer} from "react-router-bootstrap"
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";

import Galleries from "Galleries";
import EditGallery from "EditGallery";
import Posts from "Posts";
import CreatePost from "CreatePost";
import EditPost from "EditPost";
import CreateGallery from "CreateGallery";
import Pages from "Pages";
import CreatePage from "CreatePage";
import EditPage from "EditPage";

export default class EditorIndex extends Component {
  state = {
    user: null
  };

  render() {
    return (
      <div>
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
              <button className="btn btn-primary" onClick={this.props.onLogout}>Logout</button>
            </Navbar.Brand>
          </Navbar>
          <Container fluid={true}>
            <Switch>
              {/*<Route path="/" exact component={Home} />*/}
              <Route path="/galleries" exact>
                <Galleries />
              </Route>
              <Route path="/galleries/create" exact>
                <CreateGallery />
              </Route>
              <Route path="/galleries/:galleryId">
                <EditGallery />
              </Route>
              <Route path="/posts" exact>
                <Posts />
              </Route>
              <Route path="/posts/create" exact>
                <CreatePost />
              </Route>
              <Route path="/posts/:postId">
                <EditPost />
              </Route>
              <Route path="/pages" exact>
                <Pages />
              </Route>
              <Route path="/pages/create" exact>
                <CreatePage />
              </Route>
              <Route path="/pages/:pageId">
                <EditPage />
              </Route>
            </Switch>
          </Container>
        </Router>
      </div>
    );
  }

}
