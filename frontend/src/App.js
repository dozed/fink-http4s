import AdminApp from "admin/App";

import React, {Component} from 'react';
import Container from "react-bootstrap/Container";
import {LinkContainer} from 'react-router-bootstrap'
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

  componentDidMount() {
    // fetch('/api/getUsername')
    //   .then(res => res.json())
    //   .then(user => this.setState({ username: user.username }));
  }

  render() {
    const {username} = this.state;
    return (
      <Router>
        <Switch>
          {/*<Route path="/galleries" exact component={AdminGalleries} />*/}
          {/*<Route path="/galleries/:galleryId" component={AdminEditGallery} />*/}
          <Route path="/admin" component={AdminApp} />
        </Switch>
      </Router>
    );
  }

}
