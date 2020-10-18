
import React from "react";
import {Link} from "react-router-dom";

export default class Layout extends React.Component {

  render() {
    return (
      <div id="page" className="hfeed">
        <header id="branding" role="banner" className="clearfix">
          <hgroup>
            <h1 id="site-title">
              <span>
                <Link to="/" title="Fink" rel="home">Fink</Link>
              </span>
            </h1>
          </hgroup>

          <nav id="access" role="navigation">
            <h1 className="section-heading">Main menu</h1>
            <div className="skip-link screen-reader-text">
              <a href="#content" title="Skip to content">Skip to content</a>
            </div>
            <div className="menu-primary-container">
              <ul id="menu-primary" className="menu">
                <li className="menu-item menu-item-type-post_type menu-item-object-page" id="menu-item-1">
                  <a href="/page/shortlink">page.title</a>
                </li>
              </ul>
            </div>
          </nav>
        </header>

        <div id="main" className="clearfix">
          <div id="primary">
            <div id="content" role="main">
              {this.props.children}
            </div>
          </div>

          <div id="secondary" className="widget-area" role="complementary">
            <aside id="archives" className="widget">
              <h1 className="widget-title">Archives</h1>
              <ul>
                {this.props.posts.map(p => {
                  return (<li key={`post-item-${p.id}`}>
                    <Link to={`/posts/${p.id}`}>{p.title}</Link>
                  </li>)
                })}
              </ul>
            </aside>

            <aside id="meta" className="widget">
              <h1 className="widget-title">Meta</h1>
              <ul>
                <li>
                  <a href="http://localhost:3000/">Log in</a>
                </li>
              </ul>
            </aside>
          </div>
        </div>

        <footer id="colophon" role="contentinfo">
          <div id="site-generator">
            Powered by <a href="https://github.com/dozed/fink" rel="generator" title="Publishing Platform">Fink</a> and <a href="http://wptheming.com/foghorn/" rel="generator" title="Download the Foghorn Theme">Foghorn</a>
          </div>
        </footer>
      </div>
    );
  }

}