import {deletePage, getPages} from "../../frontend-shared/api";

import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
import BootstrapTable from "react-bootstrap-table-next";
import moment from "moment";
import {addToast} from "ToastContainer";
import {showConfirmation} from "ConfirmationDialog";

export default class Pages extends Component {
  state = {
    pages: [],
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
            <Button onClick={() => this.editPage(row)}>Edit</Button>
            {" "}
            <Button variant="secondary" onClick={() => this.deletePage(row)}>Delete</Button>
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
          <Button onClick={() => this.createPage()}>Create Page</Button>
        </ButtonToolbar>
        <BootstrapTable keyField={"id"} data={this.state.pages} columns={columns} bordered={false} bootstrap4={true} />
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

  editPage(page) {
    this.props.history.push(`/pages/${page.id}`);
  }

  createPage() {
    this.props.history.push(`/pages/create`);
  }

  deletePage(page) {
    showConfirmation(
      "Do you really want to delete this page?",
      () => deletePage(page.id).then(() => this.loadPages())
    );
  }

  loadPages() {
    getPages().then(
      res => this.setState({ pages: res.body }),
      err => {
        addToast("Error", "There was an error loading pages.");
      }
    );
  }

  componentDidMount() {
    this.loadPages();
  }

}
