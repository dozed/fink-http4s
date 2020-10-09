import {getPage, updatePage} from "../../frontend-shared/api";

import React, {Component} from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";

export default class EditPage extends Component {
  state = {
    title: "",
    text: "",
  };

  componentDidMount() {

    this.loadPage(this.props.match.params.pageId);

  }

  loadPage(galleryId) {
    getPage(this.props.match.params.pageId)
      .then(g => this.setState({ pageId: g.page.id, title: g.page.title, text: g.page.text }));
  }

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
            <Form.Control as="textarea" rows="3" placeholder="Enter text" onChange={(e) => this.onChangeText(e)} value={this.state.text} />
          </Form.Group>

          <ButtonToolbar>
            <Button variant="secondary" onClick={() => this.props.history.goBack()}>Cancel</Button>
            <Button variant="primary" onClick={() => this.updatePage()}>Submit</Button>
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

  updatePage() {
    updatePage(this.state.pageId, this.state.title, this.state.text, [], this.state.title)
      .then((res) => {
        this.props.history.goBack();
      });
  }

}
