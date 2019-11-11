import React, { Component } from "react";
import {getGalleries, getGallery, getPost, updateGallery, updatePost, uploadImage, uploadImageToGallery} from "api";
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

export default class EditPost extends Component {
  state = {
    title: "",
    text: "",
  };

  componentDidMount() {

    this.loadPost(this.props.match.params.postId);

  }

  loadPost(galleryId) {
    getPost(this.props.match.params.postId)
      .then(g => this.setState({ postId: g.post.id, title: g.post.title, text: g.post.text }));
  }

  render() {
    return (
      <div className="post-edit">
        <h2>Edit Post Infos</h2>
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
            <Button variant="primary" onClick={() => this.updatePost()}>Submit</Button>
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

  updatePost() {
    updatePost(this.state.postId, this.state.title, this.state.text, [], this.state.title)
      .then((res) => {
        this.props.history.goBack();
      });
  }

}
