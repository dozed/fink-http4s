import {getGallery} from "../../frontend-shared/api";
import {mkImageUrlKeepRatio} from "../../frontend-shared/urls";

import React, { useState, useEffect } from "react";
import ReactMarkdown from "react-markdown";
import remarkBreaks from "remark-breaks";

export const Gallery = ({ match }) => {

  const [gallery, setGallery] = useState(null);
  const [images, setImages] = useState([]);

  const galleryId = match.params.galleryId;

  useEffect(() => {
    getGallery(galleryId).then(res => {
      const g = res.body;
      setGallery(g.gallery);
      setImages(g.images);
    });
  }, [galleryId]);

  return (
    gallery &&
      <div className="content-wrap clearfix">
        <article id="post-2" className={`gallery-${gallery.id} page type-page status-publish hentry`}>
          <header className="entry-header">
            <h1 className="entry-title">{gallery.title}</h1>
          </header>
          <div className="entry-content">
            <ReactMarkdown source={gallery.text} plugins={[remarkBreaks]}/>
          </div>
          <div className="images">
            {images.map(i => <img key={`img-${i.id}`} src={mkImageUrlKeepRatio(i.hash, i.extension, 300)} alt=""/>)}
          </div>
        </article>
      </div>
  );
}
