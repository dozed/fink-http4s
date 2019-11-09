import React, { Component } from 'react';
import ReactImage from './react.png';
import {getGalleries, getGallery, updateGallery, uploadImage, uploadImageToGallery} from "./api";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import NavDropdown from "react-bootstrap/NavDropdown";
import Form from "react-bootstrap/Form";
import FormControl from "react-bootstrap/FormControl";
import Button from "react-bootstrap/Button";
import { LinkContainer } from 'react-router-bootstrap'
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  useRouteMatch,
  useParams
} from "react-router-dom";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";

const UploadPictureButton = ({onChange}) => (
  <label className="upload-label btn btn-space btn-default">
    <input type="file" onChange={onChange}/>
    <span>Change picture</span>
  </label>
);

class UploadImage extends Component {
  state = {
    title: null,
    imageData: null,
    uploading: false
  };

  render() {
    return (
      <div>
        <div>
          Title:
          <input type="text" onChange={this.onChangeTitle.bind(this)}/>
        </div>
        <div>
          <UploadPictureButton onChange={this.onChangePicture.bind(this)}/>
        </div>
        <div>
          <Button onClick={this.uploadImage.bind(this)}>Add</Button>
        </div>
      </div>
    );
  }

  uploadImage() {
    uploadImageToGallery(this.props.galleryId, this.state.title, this.state.imageData)
      .then(() => this.props.onUploadedImageToGallery());
  }

  onChangeTitle(e) {
    this.setState({
      title: e.target.value
    });
  }

  onChangePicture(e) {

    const file = e.currentTarget.files[0];
    const reader = new FileReader();

    reader.onload = (e) => {
      this.setState({
        imageData: e.target.result
      });

      // uploadImage(e.target.result);
    };

    reader.readAsDataURL(file);
  }
}

export default class Galleries extends Component {
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
            <Button variant="secondary" onClick={() => this.hideCreateGallery()}>Cancel</Button>
            <Button variant="primary" onClick={() => this.updateGallery()}>Submit</Button>
          </ButtonToolbar>
        </Form>

        <hr/>

        <UploadImage galleryId={this.state.galleryId} onUploadedImageToGallery={() => this.onUploadedImageToGallery()}/>

        <hr/>

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
