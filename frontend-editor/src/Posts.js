import {deletePost, getPosts} from "../../frontend-shared/api";

import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
import Table from "react-bootstrap/Table";
import moment from "moment";

const PostLine = ({info, onEdit, onDelete}) => (
  <tr>
    <td>{info.title}</td>
    <td>{moment(info.date).format("MMM Do YY")}</td>
    <td>
      <Button onClick={() => onEdit(info)}>Edit</Button>
      {" "}
      <Button onClick={() => onDelete(info)}>Delete</Button>
    </td>
  </tr>
);

export default class Posts extends Component {
  state = {
    posts: [],
  };

  render() {
    return (
      <div className="items-list">
        <ButtonToolbar>
          <Button onClick={() => this.createPost()}>Create Post</Button>
        </ButtonToolbar>
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
            {this.state.posts.map(x =>
              <PostLine key={`post-${x.id}`} info={x} onEdit={(p) => this.editPost(p)} onDelete={(p) => this.deletePost(p)} />
            )}
            </tbody>
          </Table>
        </div>
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

  editPost(post) {
    this.props.history.push(`/posts/${post.id}`);
  }

  createPost() {
    this.props.history.push(`/posts/create`);
  }

  deletePost(post) {
    deletePost(post.id).then(() => this.loadPosts());
  }

  loadPosts() {
    getPosts().then(res => this.setState({ posts: res.body }));
  }

  componentDidMount() {
    this.loadPosts();
  }

}
