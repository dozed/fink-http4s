import {getPost, getPosts} from "api";

import React from "react";
import ReactMarkdown from "react-markdown";
import { Link } from "react-router-dom";
import moment from "moment";
import remarkBreaks from "remark-breaks";

export default class Post extends React.Component {

  state = {
    post: null,
    author: null,
    tags: []
  };

  componentDidUpdate(prevProps, prevState, snapshot) {
    const postId = this.props.match.params.postId;

    if (prevProps.match.params.postId !== postId) {
      getPost(postId).then((res) => {
        this.setState(res.body);
      });
    }
  }

  componentDidMount() {
    document.body.classList.add('single');

    const postId = this.props.match.params.postId;

    getPost(postId).then((res) => {
      this.setState(res.body);
    });
  }

  componentWillUnmount() {
    document.body.classList.remove('single');
  }

  render() {
    const post = this.state.post;
    const author = this.state.author;
    const tags = this.state.tags;

    if (!post) {
      return <div />;
    }

    const postdate = moment(post.date);
    const datetime = postdate.format("MMMM DD, YYYY");
    const month = postdate.format("MMMM");
    const day = postdate.format("DD");
    const year = postdate.format("YYYY");

    return (
      <div className={"content-wrap clearfix"}>
        <article id="post" className={"type-post status-publish format-aside hentry category-uncategorized tag-post-formats clearfix"}>
          <header className={"entry-header"}>
            <h1 className={"entry-title"}>{post.title}</h1>
            <div className={"entry-meta"}>
              <span className={"sep"}>Posted </span>
              <a href={`/posts/${post.id}`}>
                <time className={"entry-date"} dateTime={datetime}>{datetime}</time>
              </a>
            </div>
          </header>

          <div className={"entry-content"}>
            <ReactMarkdown source={post.text} plugins={[remarkBreaks]}/>
          </div>
        </article>

        <footer className={"entry-meta"}>
          <div className={"post-date"}>
            <time className={"entry-date"} pubdate={""} dateTime={datetime}>
              <span className={"month"}>{month} </span>
              <span className={"day"}>{day}</span>
              <span className={"sep"}>, </span>
              <span className={"year"}>{year}</span>
            </time>
          </div>

          {
            (tags && tags.length > 0) &&
              <div className={"tags"}>
                <span>Tagged:</span>
                {
                tags.map(t => {
                  return (
                    <Link to={`/tag/${t.name}`} rel={"tag"} title={`View all posts in ${t.name}`}>{t.name}</Link>
                  );
                })
              }</div>
          }

          {
            (!tags || tags.length === 0) &&
              <div className={"categories"}>
                <span>Categorized: </span>
                <Link rel="category tag" title={"View all posts in Uncategorized"} to="/category/uncategorized/">Uncategorized</Link>
              </div>
          }
        </footer>
      </div>
    );
  }
}
