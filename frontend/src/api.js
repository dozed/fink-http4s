
export const uploadImage = (title, imageData) => {

  const data = {
    title: title,
    imageData: imageData
  };

  fetch("/api/images", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  }).then(
    response => response.json()
  ).then(
    success => console.log(success)
  ).catch(
    error => console.log(error)
  );
};
