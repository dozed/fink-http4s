import {getPost, updatePost} from "../../frontend-shared/api";

import React, {Component} from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
import {Controlled as CodeMirror} from "react-codemirror2";
import "codemirror/mode/markdown/markdown";
import {addToast} from "ToastContainer";

export default class EditPost extends Component {
  state = {
    title: "",
    text: "",
  };

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

  onChangeText(value) {
    this.setState({
      text: value
    });
  }

  updatePost() {
    updatePost(this.state.postId, this.state.title, this.state.text, [], this.state.title)
      .then((res) => {
        this.props.history.goBack();
      }, (err) => {
        addToast("Error", "There was an error updating your post.");
      });
  }

  loadPost() {
    getPost(this.props.match.params.postId)
      .then(res => {
        const p = res.body;
        this.setState({ postId: p.post.id, title: p.post.title, text: p.post.text })
      }, (err) => {
        addToast("Error", "There was an error loading your post.");
      });
  }

  componentDidMount() {
    this.loadPost();
  }

}
