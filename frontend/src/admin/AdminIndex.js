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

import Home from "admin/Home";
import Galleries from "admin/Galleries";
import EditGallery from "admin/EditGallery";
import Posts from "admin/Posts";
import CreatePost from "admin/CreatePost";
import EditPost from "admin/EditPost";
import CreateGallery from "admin/CreateGallery";
import Pages from "admin/Pages";
import CreatePage from "admin/CreatePage";
import EditPage from "admin/EditPage";
import {logout} from "../api";

export default class AdminIndex extends Component {
  state = {
    user: null
  };

  render() {
    return (
      <div>
        <Router>
          <Navbar bg="light" expand="lg">
            <LinkContainer to="/admin">
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
            <Navbar.Brand className="pull-right">
              <button className="btn btn-primary" onClick={this.props.onLogout}>Logout</button>
            </Navbar.Brand>
          </Navbar>
          <Container fluid={true}>
            <Switch>
              <Route path="/admin/" exact component={Home} />
              <Route path="/admin/galleries" exact component={Galleries} />
              <Route path="/admin/galleries/create" exact component={CreateGallery} />
              <Route path="/admin/galleries/:galleryId" component={EditGallery} />
              <Route path="/admin/posts" exact component={Posts} />
              <Route path="/admin/posts/create" exact component={CreatePost} />
              <Route path="/admin/posts/:postId" component={EditPost} />
              <Route path="/admin/pages" exact component={Pages} />
              <Route path="/admin/pages/create" exact component={CreatePage} />
              <Route path="/admin/pages/:pageId" component={EditPage} />
            </Switch>
          </Container>
        </Router>
      </div>
    );
  }

}
