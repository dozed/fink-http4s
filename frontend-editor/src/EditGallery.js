import { getGallery, updateGallery, uploadImageToGallery } from "../../frontend-shared/api";

import React, {Component} from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
import {Controlled as CodeMirror} from "react-codemirror2";
import "codemirror/mode/markdown/markdown";

class UploadImage extends Component {
  state = {
    title: "",
    imageData: null,
    uploading: false
  };

  render() {
    return (
      <Form>
        <h2>Upload New Image</h2>
        <Form.Group controlId="formImageTitle">
          <Form.Label>Title</Form.Label>
          <Form.Control type="text" placeholder="Enter title" onChange={(e) => this.onChangeTitle(e)} value={this.state.title} />
        </Form.Group>
        <div>
          <label className="upload-label btn btn-space btn-default">
            <input type="file" onChange={(e) => this.onChangePicture(e)}/>
            {/*<span>Change picture</span>*/}
          </label>
        </div>
        <div>
          <Button onClick={this.uploadImage.bind(this)}>Add</Button>
        </div>
      </Form>
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

export default class EditGallery extends Component {
  state = {
    title: "",
    text: "",
    images: [],
  };

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
            <Form.Label>Text</Form.Label>
            <CodeMirror
              value={this.state.text}
              options={{
                mode: "markdown",
                theme: "neat",
                inputStyle: "contenteditable",
                lineNumbers: true,
                lineWrapping: true,
              }}
              onBeforeChange={(editor, data, value) => {
                this.onChangeText(value);
              }}
            />
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

  onChangeText(value) {
    this.setState({
      text: value
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
      .then(res => {
        this.loadGallery(this.props.match.params.galleryId);
        // this.setState({ galleryId: g.gallery.id, title: g.gallery.title, text: g.gallery.text })
      });
  }

  loadGallery() {
    getGallery(this.props.match.params.galleryId)
      .then(res => {
        const g = res.body;
        this.setState({ galleryId: g.gallery.id, title: g.gallery.title, text: g.gallery.text, images: g.images })
      });
  }

  componentDidMount() {

    this.loadGallery();

  }

}
