import React, {Component} from "react";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";

import Home from "Home";
import Galleries from "Galleries";
import Gallery from "Gallery";

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
        </Switch>
      </Router>
    );
  }

}
