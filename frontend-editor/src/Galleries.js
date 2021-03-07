import {deleteGallery, getGalleries} from "../../frontend-shared/api";

import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
import BootstrapTable from "react-bootstrap-table-next";
import moment from "moment";
import {addToast} from "ToastContainer";
import {showConfirmation} from "ConfirmationDialog";


export default class Galleries extends Component {
  state = {
    galleries: [],
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
            <Button onClick={() => this.editGallery(row)}>Edit</Button>
            {" "}
            <Button variant="secondary" onClick={() => this.deleteGallery(row)}>Delete</Button>
          </div>
        )
      },
      headerStyle: (column, colIndex) => {
        return { width: "200px" };
      }
    }];

    return (
      <div className="galleries-list items-list">
        <ButtonToolbar>
          <Button onClick={() => this.createGallery()}>Create Gallery</Button>
        </ButtonToolbar>
        <BootstrapTable keyField={"id"} data={this.state.galleries} columns={columns} bordered={false} bootstrap4={true} />
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
