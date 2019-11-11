import {createGallery, createPost, getGalleries, getPosts, uploadImage} from "api";

import React, { Component } from "react";
import Button from "react-bootstrap/Button";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
import Form from "react-bootstrap/Form";
import Table from "react-bootstrap/Table";
import moment from "moment";

const PostLine = ({info, onEdit}) => (
  <tr>
    <td>{info.title}</td>
    <td>{moment(info.date).format("MMM Do YY")}</td>
    <td>
      <Button onClick={() => onEdit(info)}>Edit</Button>
    </td>
  </tr>
);

export default class Posts extends Component {
  state = {
    posts: [],
    createPost: false,
    title: null,
    text: null,
  };

  componentDidMount() {
    getPosts().then(xs => this.setState({ posts: xs }));
  }

  render() {
    // const { username } = this.state;
    return (
      <div>
        {!this.state.createPost && <div>
          <Button onClick={() => this.showCreatePost()}>Create Gallery</Button>
        </div>
        }
        {this.state.createPost &&
          <div>
            <Form>
              <Form.Group controlId="formTitle">
                <Form.Label>Title</Form.Label>
                <Form.Control type="text" placeholder="Enter title" onChange={(e) => this.onChangeTitle(e)} />
              </Form.Group>

              <Form.Group controlId="formText">
                <Form.Label>Description</Form.Label>
                <Form.Control as="textarea" rows="3" placeholder="Enter description" onChange={(e) => this.onChangeText(e)} />
              </Form.Group>

              <ButtonToolbar>
                <Button variant="secondary" onClick={() => this.hideCreatePost()}>Cancel</Button>
                <Button variant="primary" onClick={() => this.createGallery()}>Submit</Button>
              </ButtonToolbar>
            </Form>
          </div>
        }
        <div>
          <Table>
            <thead>
              <tr>
                <th>Title</th>
                <th>Date Created</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
            {this.state.posts.map(x => <PostLine key={`post-${x.id}`} info={x} onEdit={(g) => this.editPost(g)}/>)}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }

  editPost(x) {
    this.props.history.push(`/posts/${x.id}`);
  }

  showCreatePost() {
    this.setState({ createPost: true });
  }

  hideCreatePost() {
    this.setState({ createPost: false });
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
        this.hideCreatePost();
        getPosts().then(xs => this.setState({ posts: xs }));
      });
  }


}
