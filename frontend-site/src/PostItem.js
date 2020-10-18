import React from "react";
import ReactMarkdown from "react-markdown";
import { Link } from "react-router-dom";
import moment from "moment";

export class PostItem extends React.Component {
  render() {
    const post = this.props.post;

    return (
      <div className={"content-wrap"}>
        <article className={"post type-post status-publish format-standard hentry"}>
          <div>
            <header className={"entry-header"}>
              <h1 className={"entry-title"}>
                <Link to={`/posts/${post.id}`} rel={"bookmark"}>{post.title}</Link>
              </h1>
              <div className={"entry-meta"}>
                <Link to={`/posts/${post.id}`}>
                  <time className={"entry-date"}>{moment(post.date).format("MMMM DD, YYYY")}</time>
                </Link>
              </div>
            </header>

            {(post.text.length > 200) &&
              <div className={"entry-summary"}>
                <div>
                  <ReactMarkdown source={post.text.substr(0, 200) + " …"} />
                </div>
                <Link to={`/posts/${post.id}`}>
                  Continue reading
                  <span className={"meta-nav"}>→</span>
                </Link>
              </div>
            }

            {(post.text.length <= 200) &&
              <div>
                <div>
                  <ReactMarkdown source={post.text} />
                </div>
              </div>
            }

            <footer className={"entry-meta"}>
              <span className={"entry-utility-prep entry-utility-prep-cat-links"}>
                {"Posted in "}
              </span>

              {
                (post.tags && post.tags.length > 0) &&
                  post.tags.map(t => {
                    return (
                      <Link to={`/tag/${t.name}`} rel={"tag"} title={`View all posts in ${t.name}`}>{t.name}</Link>
                    );
                  })
              }

              {
                (!post.tags || post.tags.length === 0) &&
                  <Link to={"/category/uncategorized"}>Uncategorized</Link>
              }
            </footer>
          </div>
        </article>
      </div>
    );
  }
}
