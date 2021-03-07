import {deletePost, getPosts} from "../../frontend-shared/api";

import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
import BootstrapTable from "react-bootstrap-table-next";
import moment from "moment";
import {addToast} from "ToastContainer";
import {showConfirmation} from "ConfirmationDialog";


export default class Posts extends Component {
  state = {
    posts: [],
  };

  render() {
    const columns = [{
      dataField: "title",
      text: "Title",
      sort: true
    }, {
      dataField: "date",
      text: "Date Created",
      formatter: (rowContent, row) => {
        return moment(row.date).format("MMM Do YY");
      },
      headerStyle: (column, colIndex) => {
        return { width: "200px" };
      },
      sort: true
    }, {
      dataField: "actions",
      text: "Actions",
      formatter: (rowContent, row) => {
        return (
          <div>
            <Button onClick={() => this.editPost(row)}>Edit</Button>
            {" "}
            <Button variant="secondary" onClick={() => this.deletePost(row)}>Delete</Button>
          </div>
        )
      },
      headerStyle: (column, colIndex) => {
        return { width: "200px" };
      }
    }];

    return (
      <div className="items-list">
        <ButtonToolbar>
          <Button onClick={() => this.createPost()}>Create Post</Button>
        </ButtonToolbar>
        <BootstrapTable keyField={"id"} data={this.state.posts} columns={columns} bordered={false} bootstrap4={true} />
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
    showConfirmation(
      "Do you really want to delete this post?",
      () => deletePost(post.id).then(() => this.loadPosts())
    );
  }

  loadPosts() {
    getPosts().then(
      res => this.setState({ posts: res.body }),
      err => {
        addToast("Error", "There was an error loading posts.");
      }
    );
  }

  componentDidMount() {
    this.loadPosts();
  }

}
