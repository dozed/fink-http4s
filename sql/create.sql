
CREATE TABLE IF NOT EXISTS users (
  id bigint PRIMARY KEY,
  name text NOT NULL,
  password text NOT NULL
);

CREATE TABLE IF NOT EXISTS pages (
  id bigint PRIMARY KEY,
  date bigint,
  title text NOT NULL,
  authorId bigint UNIQUE REFERENCES users(id) ON DELETE CASCADE,
  shortlink text NOT NULL,
  value text NOT NULL
);

CREATE TABLE IF NOT EXISTS posts (
  id bigint PRIMARY KEY,
  date bigint,
  title text NOT NULL,
  authorId bigint UNIQUE REFERENCES users(id) ON DELETE CASCADE,
  shortlink text NOT NULL,
  value text NOT NULL
);

CREATE TABLE IF NOT EXISTS images (
  id bigint PRIMARY KEY,
  date bigint,
  title text NOT NULL,
  authorId bigint UNIQUE REFERENCES users(id) ON DELETE CASCADE,
  hash text NOT NULL,
  contentType text NOT NULL,
  fileName text NOT NULL
);

CREATE TABLE IF NOT EXISTS galleries (
  id bigint PRIMARY KEY,
  coverId bigint UNIQUE REFERENCES images(id) ON DELETE CASCADE,
  date bigint,
  title text NOT NULL,
  authorId bigint UNIQUE REFERENCES users(id) ON DELETE CASCADE,
  shortlink text NOT NULL,
  description text NOT NULL
);

CREATE TABLE IF NOT EXISTS tags (
  id bigint PRIMARY KEY,
  value text NOT NULL
);

CREATE TABLE IF NOT EXISTS pages_tags (
  pageId bigint UNIQUE REFERENCES pages(id) ON DELETE CASCADE,
  tagId bigint UNIQUE REFERENCES tags(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS posts_tags (
  postId bigint UNIQUE REFERENCES posts(id) ON DELETE CASCADE,
  tagId bigint UNIQUE REFERENCES tags(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS galleries_images (
  galleryId bigint UNIQUE REFERENCES galleries(id) ON DELETE CASCADE,
  imageId bigint UNIQUE REFERENCES images(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS galleries_tags (
  galleryId bigint UNIQUE REFERENCES galleries(id) ON DELETE CASCADE,
  tagId bigint UNIQUE REFERENCES tags(id) ON DELETE CASCADE
);






COMMIT;

ANALYZE;
