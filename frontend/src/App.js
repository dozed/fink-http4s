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

import Home from "frontend/Home";
import Galleries from "frontend/Galleries";
import Gallery from "frontend/Gallery";
import AdminApp from "admin/AdminApp";

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
          <Switch>
            <Route path="/" exact component={Home} />
            <Route path="/galleries" exact component={Galleries} />
            <Route path="/galleries/:galleryId" component={Gallery} />
            {/*<Route path="/posts" exact component={Posts} />*/}
            {/*<Route path="/posts/:postId" component={Post} />*/}
            {/*<Route path="/pages" exact component={Pages} />*/}
            {/*<Route path="/pages/:pageId" component={Page} />*/}
            <Route path="/admin" component={AdminApp} />
          </Switch>
        </Router>
      </div>
    );
  }

}
