import {deleteGallery, getGalleries} from "../../frontend-shared/api";

import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import Table from "react-bootstrap/Table";
import moment from "moment";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
import {addToast} from "./ToastContainer";
import {showConfirmation} from "./ConfirmationDialog";

const GalleryLine = ({info, onEdit, onDelete}) => (
  <tr>
    <td>{info.title}</td>
    <td>{moment(info.date).format("MMM Do YY")}</td>
    <td>
      <Button onClick={() => onEdit(info)}>Edit</Button>
      {" "}
      <Button variant="secondary" onClick={() => onDelete(info)}>Delete</Button>
    </td>
  </tr>
);

export default class Galleries extends Component {
  state = {
    galleries: [],
  };

  render() {
    // const { username } = this.state;
    return (
      <div className="galleries-list items-list">
        <ButtonToolbar>
          <Button onClick={() => this.createGallery()}>Create Gallery</Button>
        </ButtonToolbar>
        <div>
          <Table>
            <thead>
              <tr>
                <th>Title</th>
                <th width="200px">Date Created</th>
                <th width="200px">Actions</th>
              </tr>
            </thead>
            <tbody>
            {this.state.galleries.map(x =>
              <GalleryLine key={`gallery-${x.id}`} info={x} onEdit={(g) => this.editGallery(g)} onDelete={(g) => this.deleteGallery(g) }/>
            )}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }

  editGallery(g) {
    this.props.history.push(`/galleries/${g.id}`);
  }

  createGallery() {
    this.props.history.push(`/galleries/create`);
  }

  deleteGallery(g) {
    showConfirmation(
      "Do you really want to delete this gallery?",
      () => deleteGallery(g.id).then(() => this.loadGalleries())
    );
  }

  loadGalleries() {
    getGalleries().then(
      res => this.setState({ galleries: res.body }),
      err => addToast("Error", "There was an error loading posts.")
    );
  }

  componentDidMount() {
    this.loadGalleries();
  }

}
