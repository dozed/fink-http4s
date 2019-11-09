import Galleries from "./Galleries";
import Home from "./Home";
import EditGallery from "./EditGallery";

import React, {Component} from 'react';
import {uploadImage} from "./api";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import Button from "react-bootstrap/Button";
import {LinkContainer} from 'react-router-bootstrap'
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  useRouteMatch,
  useParams
} from "react-router-dom";

import './app.css';

const UploadPictureButton = ({onChange}) => (
  <label className="upload-label btn btn-space btn-default">
    <input type="file" onChange={onChange}/>
    <span>Change picture</span>
  </label>
);

class UploadImage extends Component {
  state = {
    title: null,
    imageData: null,
    uploading: false
  };

  render() {
    return (
      <div>
        <h1>fink</h1>
        <div>
          Title:
          <input type="text" onChange={this.onChangeTitle.bind(this)}/>
        </div>
        <div>
          <UploadPictureButton onChange={this.onChangePicture.bind(this)}/>
        </div>
        <div>
          <Button onClick={this.uploadImage.bind(this)}/>
        </div>
      </div>
    );
  }

  uploadImage() {
    uploadImage(this.state.title, this.state.imageData);
  }

  onChangeTitle(e) {
    this.setState({
      title: e.target.value
    });
  }

  onChangePicture(e) {

    const file = e.currentTarget.files[0];
    const reader = new FileReader();

    reader.onload = (e) => {
      this.setState({
        imageData: e.target.result
      });

      // uploadImage(e.target.result);
    };

    reader.readAsDataURL(file);
  }
}

export default class App extends Component {
  state = {
    title: null,
    imageData: null,
    uploading: false
  };

  componentDidMount() {
    // fetch('/api/getUsername')
    //   .then(res => res.json())
    //   .then(user => this.setState({ username: user.username }));
  }

  render() {
    const {username} = this.state;
    return (
      <div>
        <Router>
          <Navbar bg="light" expand="lg">
            <LinkContainer to="/">
              <Navbar.Brand>fink</Navbar.Brand>
            </LinkContainer>
            <Navbar.Toggle aria-controls="basic-navbar-nav"/>
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="mr-auto">
                <LinkContainer to="/">
                  <Nav.Link>Home</Nav.Link>
                </LinkContainer>
                <LinkContainer to="/galleries">
                  <Nav.Link>Galleries</Nav.Link>
                </LinkContainer>
              </Nav>
            </Navbar.Collapse>
          </Navbar>
          <div>
            <Switch>
              <Route path="/" exact component={Home} />
              <Route path="/galleries" exact component={Galleries} />
              <Route path="/galleries/:galleryId" component={EditGallery} />
            </Switch>
          </div>
        </Router>
      </div>
    );
  }

}
