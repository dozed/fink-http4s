import { getPosts } from "../../frontend-shared/api";

import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
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
  };

  componentDidMount() {
    getPosts().then(xs => this.setState({ posts: xs }));
  }

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

  createPost(x) {
    this.props.history.push(`/posts/create`);
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

}
