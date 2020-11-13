import { createPost } from "../../frontend-shared/api";

import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
import Form from "react-bootstrap/Form";

export default class CreatePost extends Component {
  state = {
    title: "",
    text: "",
  };

  render() {
    return (
      <Form>
        <Form.Group controlId="formTitle">
          <Form.Label>Title</Form.Label>
          <Form.Control type="text" placeholder="Enter title" onChange={(e) => this.onChangeTitle(e)} />
        </Form.Group>

        <Form.Group controlId="formText">
          <Form.Label>Text</Form.Label>
          <Form.Control as="textarea" rows="15" placeholder="Enter text" onChange={(e) => this.onChangeText(e)} />
        </Form.Group>

        <ButtonToolbar>
          <Button variant="secondary" onClick={() => this.cancel()}>Cancel</Button>
          <Button variant="primary" onClick={() => this.createPost()}>Submit</Button>
        </ButtonToolbar>
      </Form>
    );
  }

  cancel() {
    // this.props.history.push(`/posts`);
    this.props.history.goBack();
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

  createPost() {
    createPost(this.state.title, this.state.text, [], this.state.title)
      .then((res) => {
        this.props.history.goBack();
      });
  }


}
