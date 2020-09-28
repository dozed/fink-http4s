import React, { Component } from "react";
import {getGalleries, getGallery, updateGallery, uploadImage, uploadImageToGallery} from "api";
import Form from "react-bootstrap/Form";
import FormControl from "react-bootstrap/FormControl";
import Button from "react-bootstrap/Button";
import { LinkContainer } from "react-router-bootstrap"
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  useRouteMatch,
  useParams
} from "react-router-dom";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";


export default class Gallery extends Component {
  state = {
    title: "",
    text: "",
    images: [],
  };

  componentDidMount() {

    this.loadGallery(this.props.match.params.galleryId);

  }

  loadGallery(galleryId) {
    getGallery(this.props.match.params.galleryId)
      .then(g => this.setState({ galleryId: g.gallery.id, title: g.gallery.title, text: g.gallery.text, images: g.images }));
  }

  render() {
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
