import {getPage, updatePage} from "../../frontend-shared/api";

import React, {Component} from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
import {Controlled as CodeMirror} from "react-codemirror2";
import "codemirror/mode/markdown/markdown";
import {addToast} from "./ToastContainer";

export default class EditPage extends Component {
  state = {
    title: "",
    text: "",
  };

  render() {
    return (
      <div className="post-edit">
        <h2>Edit Page Infos</h2>
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
            <Button variant="primary" onClick={() => this.updatePage()}>Save</Button>
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

  onChangeText(vaue) {
    this.setState({
      text: value
    });
  }

  updatePage() {
    updatePage(this.state.pageId, this.state.title, this.state.text, [], this.state.title)
      .then((res) => {
        this.props.history.goBack();
      }, (err) => {
        addToast("Error", "There was an error updating your page.");
      });
  }

  loadPage() {
    getPage(this.props.match.params.pageId)
      .then(res => {
        const p = res.body;
        this.setState({ pageId: p.page.id, title: p.page.title, text: p.page.text })
      }, (err) => {
        addToast("Error", "There was an error loading your page.");
      });
  }

  componentDidMount() {

    this.loadPage();

  }

}
