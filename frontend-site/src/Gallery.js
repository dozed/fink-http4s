import {getGallery, updateGallery} from "api";

import React, { Component } from "react";
import ReactMarkdown from "react-markdown";
import remarkBreaks from "remark-breaks";

export default class Gallery extends Component {
  state = {
    gallery: null,
    images: [],
  };

  componentDidMount() {

    const galleryId = this.props.match.params.galleryId;

    getGallery(galleryId).then(g => {
      this.setState({
        gallery: g.gallery,
        images: g.images
      });
    });

  }

  render() {
    return (
      this.state.gallery &&
        <div className="content-wrap clearfix">
          <article id="post-2" className={`gallery-${this.state.gallery.id} page type-page status-publish hentry`}>
            <header className="entry-header">
              <h1 className="entry-title">{this.state.gallery.title}</h1>
            </header>
            <div className="entry-content">
              <ReactMarkdown source={this.state.gallery.text} plugins={[remarkBreaks]}/>
            </div>
            <div className="images">
              {this.state.images.map(i => <img key={`img-${i.id}`} src={"/" + i.fileName} alt=""/>)}
            </div>
          </article>
        </div>
    );

    // const { username } = this.state;
    return (
      <div className="gallery-edit">
        <h2>Edit Gallery Infos</h2>
        <Form>
          <Form.Group controlId="formTitle">
            <Form.Label>Title</Form.Label>
            <Form.Control type="text" placeholder="Enter title" onChange={(e) => this.onChangeTitle(e)} value={this.state.title} />
          </Form.Group>

          <Form.Group controlId="formText">
            <Form.Label>Description</Form.Label>
            <Form.Control as="textarea" rows="3" placeholder="Enter description" onChange={(e) => this.onChangeText(e)} value={this.state.text} />
          </Form.Group>

          <ButtonToolbar>
            <Button variant="secondary" onClick={() => this.props.history.goBack()}>Cancel</Button>
            <Button variant="primary" onClick={() => this.updateGallery()}>Submit</Button>
          </ButtonToolbar>
        </Form>

        <hr/>

        <UploadImage galleryId={this.state.galleryId} onUploadedImageToGallery={() => this.onUploadedImageToGallery()}/>

        <hr/>

        <h2>Images</h2>
        <div className="images">
          {this.state.images.map(i => <img key={`img-${i.id}`} src={"/" + i.fileName} alt=""/>)}
        </div>
      </div>
    );
  }

  onChangeTitle(e) {
    this.setState({
      title: e.target.value
    });
  }

  onChangeText(e) {
    this.setState({
      text: e.target.value
    });
  }

  updateGallery() {
    updateGallery(this.state.galleryId, this.state.title, this.state.text, [], this.state.title)
      .then((res) => {
        this.loadGallery(this.props.match.params.galleryId);
      });
  }

  onUploadedImageToGallery() {

    getGallery(this.props.match.params.galleryId)
      .then(g => {
        this.loadGallery(this.props.match.params.galleryId);
        // this.setState({ galleryId: g.gallery.id, title: g.gallery.title, text: g.gallery.text })
      });

  }


}
