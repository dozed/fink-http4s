import { getGalleries } from "../../frontend-shared/api";

import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import Table from "react-bootstrap/Table";
import moment from "moment";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";

const GalleryLine = ({info, onEdit}) => (
  <tr>
    <td>{info.title}</td>
    <td>{moment(info.date).format("MMM Do YY")}</td>
    <td>
      <Button onClick={() => onEdit(info)}>Edit</Button>
    </td>
  </tr>
);

export default class Galleries extends Component {
  state = {
    galleries: [],
  };

  componentDidMount() {
    getGalleries().then(xs => this.setState({ galleries: xs }));
  }

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
                <th>Date Created</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
            {this.state.galleries.map(x => <GalleryLine key={`gallery-${x.id}`} info={x} onEdit={(g) => this.editGallery(g)}/>)}
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

}
