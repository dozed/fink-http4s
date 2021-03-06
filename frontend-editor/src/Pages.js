import {deletePage, getPages} from "../../frontend-shared/api";

import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import Table from "react-bootstrap/Table";
import moment from "moment";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
import {addToast} from "ToastContainer";
import {showConfirmation} from "ConfirmationDialog";

const PageLine = ({info, onEdit, onDelete}) => (
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

export default class Pages extends Component {
  state = {
    pages: [],
  };

  render() {
    return (
      <div className="items-list">
        <ButtonToolbar>
          <Button onClick={() => this.createPage()}>Create Page</Button>
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
            {this.state.pages.map(x =>
              <PageLine key={`page-${x.id}`}
                        info={x}
                        onEdit={(x) => this.editPage(x)}
                        onDelete={(x) => this.deletePage(x)}
              />
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
