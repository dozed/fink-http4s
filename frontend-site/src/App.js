import Home from "Home";
import Post from "Post";
import { Gallery } from "Gallery";
import Layout from "Layout";
import { Page } from "Page";
import {getGalleries, getPages, getPosts} from "api";

import React from "react";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";

export default class App extends React.Component {
  state = {
    posts: [],
    pages: [],
    galleries: []
  }

  render() {
    return (
      <Router>
        <Layout posts={this.state.posts} pages={this.state.pages} galleries={this.state.galleries}>
          <Switch>
            <Route path="/" exact component={Home} />
            <Route path="/posts/:postId" component={Post} />
            <Route path="/pages/:pageId" component={Page} />
            <Route path="/galleries/:galleryId" component={Gallery} />
          </Switch>
        </Layout>
      </Router>
    );
  }

  componentDidMount() {
    getPosts().then((res) => {
      this.setState({ posts: res.body });
    });

    getPages().then(res => {
      this.setState({ pages: res.body });
    });

    getGalleries().then(res => {
      this.setState({ galleries: res.body });
    });
  }
}
