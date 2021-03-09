import {getPage} from "api";

import React, { useState, useEffect } from "react";
import {Link} from "react-router-dom";
import ReactMarkdown from "react-markdown";
import remarkBreaks from "remark-breaks";

export const Page = (props) => {

  const [page, setPage] = useState(null);

  const pageId = props.match.params.pageId;

  useEffect(() => {
    getPage(pageId).then(res => {
      setPage(res.body.page);
    });
  }, [pageId]);

  useEffect(() => {
    document.body.classList.add("page");

    return () => {
      document.body.classList.remove("page");
    };
  }, []);

  return (
    page &&
      <div className="content-wrap clearfix">
        <article id="post-2" className={`page-${page.id} page type-page status-publish hentry`}>
          <header className="entry-header">
            <h1 className="entry-title">{page.title}</h1>
          </header>
          <div className="entry-content">
            <ReactMarkdown source={page.text} plugins={[remarkBreaks]}/>
          </div>
        </article>
      </div>
  );
};