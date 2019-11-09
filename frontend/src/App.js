import Galleries from "./Galleries";
import Home from "./Home";
import EditGallery from "./EditGallery";

import React, {Component} from 'react';
import {uploadImage} from "./api";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import Button from "react-bootstrap/Button";
import {LinkContainer} from 'react-router-bootstrap'
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  useRouteMatch,
  useParams
} from "react-router-dom";

import './app.scss';

export default class App extends Component {
  state = {
    title: null,
    imageData: null,
    uploading: false
  };

  componentDidMount() {
    // fetch('/api/getUsername')
    //   .then(res => res.json())
    //   .then(user => this.setState({ username: user.username }));
  }

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
                <LinkContainer to="/">
                  <Nav.Link>Home</Nav.Link>
                </LinkContainer>
                <LinkContainer to="/galleries">
                  <Nav.Link>Galleries</Nav.Link>
                </LinkContainer>
              </Nav>
            </Navbar.Collapse>
          </Navbar>
          <div>
            <Switch>
              <Route path="/" exact component={Home} />
              <Route path="/galleries" exact component={Galleries} />
              <Route path="/galleries/:galleryId" component={EditGallery} />
            </Switch>
          </div>
        </Router>
      </div>
    );
  }

}
