
import request from "superagent";

export const fetchMe = () => {
  return request.get("/api/auth/me");
};

export const login = (username, password) => {
  return request.post("/api/auth/login")
    .send({
      username,
      password
    });
};

export const logout = () => request.post("/api/auth/logout");

export const deleteImage = (imageId) => request.delete(`/api/images/${imageId}`);

export const uploadImage = (title, imageData) => {
  return request.post("/api/images")
    .send({
      title: title,
      imageData: imageData
    });
};

export const uploadImageToGallery = (galleryId, title, imageData) => {
  const data = {
    galleryId: galleryId,
    title: title,
    imageData: imageData
  };

  return request.post(`/api/galleries/${galleryId}/images`)
    .send(data);
};

export const sortImage = (galleryId, from, to) => {
  return request.post(`/api/galleries/${galleryId}/sorting`)
    .send({
      galleryId,
      from,
      to,
    });
};

export const getGalleries = () => {
  return request.get("/api/galleries");
};

export const getGallery = (id) => {
  return request.get(`/api/galleries/${id}`);
};

export const createGallery = (title, text, tags, shortlink) => {

  const data = {
    title: title,
    text: text,
    tags: tags,
    shortlink: shortlink
  };

  return request.post("/api/galleries")
    .send(data);

};

export const updateGallery = (id, title, text, tags, shortlink) => {

  const data = {
    id: id,
    title: title,
    text: text,
    tags: tags,
    shortlink: shortlink
  };

  return request.post(`/api/galleries/${id}`)
    .send(data);

};

export const deleteGallery = (id) => request.delete(`/api/galleries/${id}`);

export const getPosts = () => {

  return request.get("/api/posts");

};

export const getPost = (id) => {

  return request.get(`/api/posts/${id}`);

};

export const createPost = (title, text, tags, shortlink) => {

  const data = {
    title: title,
    text: text,
    tags: tags,
    shortlink: shortlink
  };

  return request.post("/api/posts")
    .send(data);

};

export const updatePost = (id, title, text, tags, shortlink) => {

  const data = {
    id: id,
    title: title,
    text: text,
    tags: tags,
    shortlink: shortlink
  };

  return request.post(`/api/posts/${id}`)
    .send(data);

};

export const deletePost = (id) => request.delete(`/api/posts/${id}`);

export const getPages = () => {

  return request.get("/api/pages");

};

export const getPage = (id) => {

  return request.get(`/api/pages/${id}`);

};

export const createPage = (title, text, tags, shortlink) => {

  const data = {
    title: title,
    text: text,
    tags: tags,
    shortlink: shortlink
  };

  return request.post("/api/pages")
    .send(data);

};

export const updatePage = (id, title, text, tags, shortlink) => {

  const data = {
    id: id,
    title: title,
    text: text,
    tags: tags,
    shortlink: shortlink
  };

  return request.post(`/api/pages/${id}`)
    .send(data);

};

export const deletePage = (id) =>
  request.delete(`/api/pages/${id}`);


