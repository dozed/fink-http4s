import {getPages} from "api";

import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import Table from "react-bootstrap/Table";
import moment from "moment";

const PageLine = ({info, onEdit}) => (
  <tr>
    <td>{info.title}</td>
    <td>{moment(info.date).format("MMM Do YY")}</td>
    <td>
      <Button onClick={() => onEdit(info)}>Edit</Button>
    </td>
  </tr>
);

export default class Pages extends Component {
  state = {
    pages: [],
  };

  componentDidMount() {
    getPages().then(xs => this.setState({ pages: xs }));
  }

  render() {
    return (
      <div>
        <div>
          <Button onClick={() => this.createPage()}>Create Page</Button>
        </div>
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
            {this.state.pages.map(x => <PageLine key={`page-${x.id}`} info={x} onEdit={(x) => this.editPage(x)}/>)}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }

  editPage(x) {
    this.props.history.push(`/pages/${x.id}`);
  }

  createPage() {
    this.props.history.push(`/pages/create`);
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
