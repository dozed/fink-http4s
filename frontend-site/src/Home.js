import {getPosts} from "api";
import {PostItem} from "PostItem";
import Layout from "Layout";

import React from "react";
import { Link } from "react-router-dom";

export default class Home extends React.Component {
  state = {
    posts: []
  }

  render() {
    return this.state.posts.map(p => {
      return <PostItem key={`post-${p.id}`} post={p} />;
    });
  }

  componentDidMount() {
    getPosts().then((res) => {
      this.setState({ posts: res.body });
    });
  }

}
