import React, { Component } from 'react';
import './app.css';
import ReactImage from './react.png';
import {getGalleries, getGallery, updateGallery, uploadImage} from "./api";
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

export default class Galleries extends Component {
  state = {
    title: "",
    text: "",
    imageData: null,
    uploading: false
  };

  componentDidMount() {

    getGallery(this.props.match.params.galleryId)
      .then(g => this.setState({ galleryId: g.gallery.id, title: g.gallery.title, text: g.gallery.text }));

  }

  render() {
    // const { username } = this.state;
    return (
      <div>
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
        this.hideCreateGallery();
        getGalleries().then(xs => this.setState({ galleries: xs }));
      });
  }


}
