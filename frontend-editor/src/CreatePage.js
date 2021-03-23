import { createPage } from "../../frontend-shared/api";

import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
import Form from "react-bootstrap/Form";
import {Controlled as CodeMirror} from "react-codemirror2";
import "codemirror/mode/markdown/markdown";
import {addToast} from "./ToastContainer";

export default class CreatePage extends Component {
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
          <Button variant="primary" onClick={() => this.createPage()}>Save</Button>
        </ButtonToolbar>
      </Form>
    );
  }

  cancel() {
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

  createPage() {
    createPage(this.state.title, this.state.text, [], this.state.title)
      .then(
        res => this.props.history.goBack(),
        err => addToast("Error", "There was an error creating your page.")
      );
  }


}
