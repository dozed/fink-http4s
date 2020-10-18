import Home from "Home";
import Post from "Post";
import Galleries from "Galleries";
import Gallery from "Gallery";
import Layout from "Layout";
import {getPosts} from "api";

import React from "react";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";

export default class App extends React.Component {
  state = {
    posts: []
  }

  render() {
    return (
      <Router>
        <Layout posts={this.state.posts}>
          <Switch>
            <Route path="/" exact component={Home} />
            {/*<Route path="/posts" exact component={Posts} />*/}
            <Route path="/posts/:postId" component={Post} />
            {/*<Route path="/posts/:postId" render={(props) => {*/}
            {/*  //do your console log or temporary testing stuff here*/}
            {/*  console.log(123);*/}
            {/*  return <Post {...props} />*/}
            {/*}} />*/}
            <Route path="/galleries" exact component={Galleries} />
            <Route path="/galleries/:galleryId" component={Gallery} />
            {/*<Route path="/pages" exact component={Pages} />*/}
            {/*<Route path="/pages/:pageId" component={Page} />*/}
          </Switch>
        </Layout>
      </Router>
    );
  }

  componentDidMount() {
    getPosts().then((posts) => {
      this.setState({ posts: posts });
    });
  }
}
