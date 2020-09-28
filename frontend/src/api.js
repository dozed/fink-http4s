
export const uploadImage = (title, imageData) => {

  const data = {
    title: title,
    imageData: imageData
  };

  return fetch("/api/images", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  }).then(
    response => response.json()
  );

};

export const uploadImageToGallery = (galleryId, title, imageData) => {

  const data = {
    galleryId: galleryId,
    title: title,
    imageData: imageData
  };

  return fetch(`/api/galleries/${galleryId}/images`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  }).then(
    response => response.json()
  );

};

export const getGalleries = () => {

  return fetch("/api/galleries").then(
    response => response.json()
  );

};

export const getGallery = (id) => {

  return fetch(`/api/galleries/${id}`).then(
    response => response.json()
  );

};

export const createGallery = (title, text, tags, shortlink) => {

  const data = {
    title: title,
    text: text,
    tags: tags,
    shortlink: shortlink
  };

  return fetch("/api/galleries", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  }).then(
    response => response.json()
  );

};

export const updateGallery = (id, title, text, tags, shortlink) => {

  const data = {
    id: id,
    title: title,
    text: text,
    tags: tags,
    shortlink: shortlink
  };

  return fetch(`/api/galleries/${id}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  }).then(
    response => response.json()
  );

};

export const getPosts = () => {

  return fetch("/api/posts").then(
    response => response.json()
  );

};

export const getPost = (id) => {

  return fetch(`/api/posts/${id}`).then(
    response => response.json()
  );

};

export const createPost = (title, text, tags, shortlink) => {

  const data = {
    title: title,
    text: text,
    tags: tags,
    shortlink: shortlink
  };

  return fetch("/api/posts", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  }).then(
    response => response.json()
  );

};

export const updatePost = (id, title, text, tags, shortlink) => {

  const data = {
    id: id,
    title: title,
    text: text,
    tags: tags,
    shortlink: shortlink
  };

  return fetch(`/api/posts/${id}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  }).then(
    response => response.json()
  );

};

export const getPages = () => {

  return fetch("/api/pages").then(
    response => response.json()
  );

};

export const getPage = (id) => {

  return fetch(`/api/pages/${id}`).then(
    response => response.json()
  );

};

export const createPage = (title, text, tags, shortlink) => {

  const data = {
    title: title,
    text: text,
    tags: tags,
    shortlink: shortlink
  };

  return fetch("/api/pages", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  }).then(
    response => response.json()
  );

};

export const updatePage = (id, title, text, tags, shortlink) => {

  const data = {
    id: id,
    title: title,
    text: text,
    tags: tags,
    shortlink: shortlink
  };

  return fetch(`/api/pages/${id}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  }).then(
    response => response.json()
  );

};

export const login = (username, password) => {

  const data = {
    username,
    password
  };

  return fetch(`/api/auth/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  });

};

export const logout = () => {

  return fetch(`/api/auth/logout`, {
    method: "POST",
  });

};

