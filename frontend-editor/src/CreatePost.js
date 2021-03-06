import { createPost } from "../../frontend-shared/api";

import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
import Form from "react-bootstrap/Form";
import {Controlled as CodeMirror} from "react-codemirror2";
import "codemirror/mode/markdown/markdown";
import TagsInput from "react-tagsinput";
import {addToast} from "ToastContainer";

export default class CreatePost extends Component {
  state = {
    title: "",
    text: "",
    tags: [],
  };

  render() {
    return (
      <Form>
        <Form.Group controlId="formTitle">
          <Form.Label>Title</Form.Label>
          <Form.Control type="text" placeholder="Enter title" onChange={(e) => this.onChangeTitle(e)} />
        </Form.Group>

        <Form.Group>
          <Form.Label>Tags</Form.Label>
          <TagsInput value={this.state.tags} onChange={(tags) => this.setState({ tags })} />
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
          <Button variant="secondary" onClick={() => this.cancel()}>Cancel</Button>
          <Button variant="primary" onClick={() => this.createPost()}>Save</Button>
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

  onChangeText(value) {
    this.setState({
      text: value
    });
  }

  createPost() {
    createPost(this.state.title, this.state.text, this.state.tags, this.state.title)
      .then(
        res => this.props.history.goBack(),
        err => addToast("Error", "There was an error creating your post.")
      );
  }


}
