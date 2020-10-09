import React, {Component} from "react";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";

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
    );
  }

}
