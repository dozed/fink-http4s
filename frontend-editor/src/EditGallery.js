import {deleteImage, getGallery, updateGallery, uploadImageToGallery} from "../../frontend-shared/api";
import {mkImageUrlFull} from "../../frontend-shared/urls";

import React, {Component, useState, forwardRef} from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import ButtonToolbar from "react-bootstrap/ButtonToolbar";
import { BsTrash } from "react-icons/bs";
import {Controlled as CodeMirror} from "react-codemirror2";
import "codemirror/mode/markdown/markdown";
import TagsInput from "react-tagsinput";
import {
  DndContext,
  DragOverlay,
  closestCenter,
} from "@dnd-kit/core";
import {
  useSortable,
  arrayMove,
  SortableContext,
} from "@dnd-kit/sortable";
import {CSS} from "@dnd-kit/utilities";
import {addToast} from "ToastContainer";

class UploadImage extends Component {
  state = {
    title: "",
    imageData: null,
    uploading: false,
  };

  render() {
    return (
      <Form>
        <h2>Upload New Image</h2>
        <Form.Group controlId="formImageTitle">
          <Form.Label>Title</Form.Label>
          <Form.Control type="text" placeholder="Enter title" onChange={(e) => this.onChangeTitle(e)} value={this.state.title} />
        </Form.Group>
        <div>
          <label className="upload-label btn btn-space btn-default">
            <input type="file" onChange={(e) => this.onChangePicture(e)}/>
            {/*<span>Change picture</span>*/}
          </label>
        </div>
        <div>
          <Button onClick={() => this.uploadImage()}>Add</Button>
        </div>
      </Form>
    );
  }

  uploadImage() {
    uploadImageToGallery(this.props.galleryId, this.state.title, this.state.imageData)
      .then(
        () => this.props.onUploadedImageToGallery(),
        (err) => {
          addToast("Error", "There was an error uploading your image.");
        });
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

const Image = ({ id, hash, extension, onDelete }) => {

  const [showOverlay, setShowOverlay] = useState(false);

  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
  } = useSortable({id: id});

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
  };

  return (
    <div className={"image"}
         onMouseEnter={() => setShowOverlay(true)} onMouseLeave={() => setShowOverlay(false)}
         ref={setNodeRef} style={style} {...attributes} {...listeners}>
      <img src={mkImageUrlFull(hash, extension)} alt=""/>
      {showOverlay &&
        <div className={"image-overlay"}>
          <div className={"delete-image"} onClick={() => onDelete(id)}>
            <BsTrash title={"Delete image"} />
          </div>
        </div>
      }
    </div>
  );
};


export const ImageItem = forwardRef(({id, hash, extension}, ref) => {
  return (
    <div className={"image"} ref={ref}>
      <img src={mkImageUrlFull(hash, extension)} alt=""/>
    </div>
  )
});

export default class EditGallery extends Component {
  state = {
    title: "",
    text: "",
    images: [],
    tags: [],
    activeImage: null,
  };

  render() {
    return (
      <div className="gallery-edit">
        <h2>Edit Gallery Infos</h2>
        <Form>
          <Form.Group controlId="formTitle">
            <Form.Label>Title</Form.Label>
            <Form.Control type="text" placeholder="Enter title" onChange={(e) => this.onChangeTitle(e)} value={this.state.title} />
          </Form.Group>

          <Form.Group>
            <Form.Label>Tags</Form.Label>
            <TagsInput value={this.state.tags} onChange={(tags) => this.setState({ tags })} />
          </Form.Group>

          <Form.Group controlId="formText">
            <Form.Label>Text</Form.Label>
            <CodeMirror
              value={this.state.text}
              options={{
                mode: "markdown",
                theme: "neat",
                inputStyle: "contenteditable",
                lineNumbers: true,
                lineWrapping: true,
              }}
              onBeforeChange={(editor, data, value) => {
                this.onChangeText(value);
              }}
            />
          </Form.Group>

          <ButtonToolbar>
            <Button variant="secondary" onClick={() => this.props.history.goBack()}>Cancel</Button>
            <Button variant="primary" onClick={() => this.updateGallery()}>Save</Button>
          </ButtonToolbar>
        </Form>

        <hr/>

        <UploadImage galleryId={this.state.galleryId} onUploadedImageToGallery={() => this.loadGallery()} />

        <hr/>

        <h2>Images</h2>
        <div className="images">
          <DndContext
            collisionDetection={closestCenter}
            onDragStart={(e) => this.handleDragStart(e)}
            onDragEnd={(e) => this.handleDragEnd(e)}
          >
            <SortableContext
              items={this.state.images.map(i => i.id)}
            >
              {this.state.images.map(i =>
                <Image key={`img-${i.id}`} id={i.id} hash={i.hash} extension={i.extension} onDelete={id => this.deleteImage(id)} />
              )}
            </SortableContext>

            <DragOverlay>
              {this.state.activeImage &&
                <ImageItem id={this.state.activeImage.id} hash={this.state.activeImage.hash} extension={this.state.activeImage.extension} />
              }
            </DragOverlay>
          </DndContext>
        </div>
      </div>
    );
  }

  handleDragStart(event) {
    const image = this.state.images.find(i => i.id === event.active.id);

    this.setState({
      activeImage: image
    });
  }

  handleDragEnd(event) {
    const {active, over} = event;

    this.setState({
      activeImage: null
    });

    if (active.id !== over.id) {
      const oldIndex = this.state.images.map(i => i.id).indexOf(active.id);
      const newIndex = this.state.images.map(i => i.id).indexOf(over.id);

      const newItems = arrayMove(this.state.images, oldIndex, newIndex);

      this.setState({
        images: newItems
      });
    }
  }

  onChangeTitle(e) {
    this.setState({
      title: e.target.value
    });
  }

  onChangeText(value) {
    this.setState({
      text: value
    });
  }

  updateGallery() {
    updateGallery(this.state.galleryId, this.state.title, this.state.text, this.state.tags, this.state.title)
      .then((res) => {
        this.props.history.goBack();
      }, (err) => {
        addToast("Error", "There was an error updating your gallery.");
      });
  }

  loadGallery() {
    getGallery(this.props.match.params.galleryId)
      .then(res => {
        const g = res.body;
        this.setState({ galleryId: g.gallery.id, title: g.gallery.title, text: g.gallery.text, images: g.images, tags: g.tags });
      }, (err) => {
        addToast("Error", "There was an error loading your gallery.");
      });
  }

  deleteImage(imageId) {
    deleteImage(imageId).then(() => this.loadGallery());
  }

  componentDidMount() {

    this.loadGallery();

  }

}
