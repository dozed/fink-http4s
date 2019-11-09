
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

