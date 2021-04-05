
CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  name text NOT NULL,
  password text NOT NULL
);

CREATE TABLE IF NOT EXISTS users_roles (
  userId bigint REFERENCES users(id) ON DELETE CASCADE,
  roleName text NOT NULL
);

CREATE TABLE IF NOT EXISTS pages (
  id SERIAL PRIMARY KEY,
  date bigint,
  title text NOT NULL,
  authorId bigint REFERENCES users(id) ON DELETE CASCADE,
  shortlink text NOT NULL,
  value text NOT NULL
);

CREATE TABLE IF NOT EXISTS posts (
  id SERIAL PRIMARY KEY,
  date bigint,
  title text NOT NULL,
  authorId bigint REFERENCES users(id) ON DELETE CASCADE,
  shortlink text NOT NULL,
  value text NOT NULL
);

CREATE TABLE IF NOT EXISTS images (
  id SERIAL PRIMARY KEY,
  date bigint,
  title text NOT NULL,
  authorId bigint REFERENCES users(id) ON DELETE CASCADE,
  hash text NOT NULL,
  extension text NOT NULL,
  fileName text NOT NULL,
  contentType text NOT NULL
);

CREATE TABLE IF NOT EXISTS galleries (
  id SERIAL PRIMARY KEY,
  coverId bigint REFERENCES images(id) ON DELETE CASCADE,
  date bigint,
  title text NOT NULL,
  authorId bigint REFERENCES users(id) ON DELETE CASCADE,
  shortlink text NOT NULL,
  description text NOT NULL
);

CREATE TABLE IF NOT EXISTS tags (
  id SERIAL PRIMARY KEY,
  value text NOT NULL
);

CREATE TABLE IF NOT EXISTS pages_tags (
  pageId bigint REFERENCES pages(id) ON DELETE CASCADE,
  tagId bigint REFERENCES tags(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS posts_tags (
  postId bigint REFERENCES posts(id) ON DELETE CASCADE,
  tagId bigint REFERENCES tags(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS galleries_images (
  galleryId bigint REFERENCES galleries(id) ON DELETE CASCADE,
  imageId bigint REFERENCES images(id) ON DELETE CASCADE,
  sort int
);

CREATE TABLE IF NOT EXISTS galleries_tags (
  galleryId bigint REFERENCES galleries(id) ON DELETE CASCADE,
  tagId bigint REFERENCES tags(id) ON DELETE CASCADE
);






COMMIT;

ANALYZE;
