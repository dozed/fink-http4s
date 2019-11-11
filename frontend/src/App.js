import Galleries from "Galleries";
import Home from "Home";
import EditGallery from "EditGallery";

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
    const {username} = this.state;
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
                <LinkContainer to="/admin/posts">
                  <Nav.Link>Posts</Nav.Link>
                </LinkContainer>
                <LinkContainer to="/admin/pages">
                  <Nav.Link>Pages</Nav.Link>
                </LinkContainer>
                <LinkContainer to="/admin/galleries">
                  <Nav.Link>Galleries</Nav.Link>
                </LinkContainer>
              </Nav>
            </Navbar.Collapse>
          </Navbar>
          <Container fluid={true}>
            <Switch>
              <Route path="/admin" exact component={Home} />
              <Route path="/admin/galleries" exact component={Galleries} />
              <Route path="/admin/galleries/:galleryId" component={EditGallery} />
            </Switch>
          </Container>
        </Router>
      </div>
    );
  }

}
