import React from "react";
import ReactMarkdown from "react-markdown";
import moment from "moment";

export class Post extends React.Component {
  render() {
    const post = this.props.post;

    return (
      <div className={"content-wrap"}>
        <article className={"post type-post status-publish format-standard hentry"}>
          <div>
            <header className={"entry-header"}>
              <h1 className={"entry-title"}>
                <a href={`/posts/${post.id}`} rel={"bookmark"}>{post.title}</a>
              </h1>
              <div className={"entry-meta"}>
                <a href={`/posts/${post.id}`}>
                  <time className={"entry-date"}>{moment(post.date).format("MMMM DD, YYYY")}</time>
                </a>
              </div>
            </header>

            {(post.text.length > 200) &&
              <div className={"entry-summary"}>
                <div>
                  <ReactMarkdown source={post.text.substr(0, 200) + " …"} />
                </div>
                <a href={`/posts/${post.id}`}>
                  Continue reading
                  <span className={"meta-nav"}>→</span>
                </a>
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
                      <a href={`/tag/${t.name}`} rel={"tag"} title={`View all posts in ${t.name}`}>{t.name}</a>
                    );
                  })
              }

              {
                (!post.tags || post.tags.length === 0) &&
                  <a href={"/category/uncategorized"}>Uncategorized</a>
              }
            </footer>
          </div>
        </article>
      </div>
    );
  }
}
