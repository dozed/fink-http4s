

export const mkImageUrlFull = (hash, extension) => `/images/${hash}-full.${extension}`;

export const mkImageUrlKeepRatio = (hash, extension, size) => `/images/${hash}-keep-ratio-${size}.${extension}`;

export const mkImageUrlSquare = (hash, extension, size) => `/images/${hash}-square-${size}.${extension}`;

