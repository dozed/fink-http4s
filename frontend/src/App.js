import Home from "Home";
import Galleries from "Galleries";
import EditGallery from "EditGallery";
import Posts from "Posts";
import CreatePost from "CreatePost";

import React, {Component} from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import {LinkContainer} from "react-router-bootstrap"
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  useRouteMatch,
  useParams
} from "react-router-dom";

import "app.scss";

export default class App extends Component {
  state = {
    title: null,
    imageData: null,
    uploading: false
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
          </Navbar>
          <Container fluid={true}>
            <Switch>
              <Route path="/" exact component={Home} />
              <Route path="/galleries" exact component={Galleries} />
              <Route path="/galleries/:galleryId" component={EditGallery} />
              <Route path="/posts" component={Posts} />
              <Route path="/posts/create" component={CreatePost} />
            </Switch>
          </Container>
        </Router>
      </div>
    );
  }

}
