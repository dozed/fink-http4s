const webpack = require("webpack");
const path = require("path");
const { merge } = require("webpack-merge");
const common = require("./webpack.common.js");

const config = {
  publicPath: "/",
  editorUrl: "http://localhost:3000/",
};

module.exports = merge(common, {
  mode: "development",
  output: {
    path: path.join(__dirname, "dist"),
    publicPath: config.publicPath,
    filename: "bundle.js",
    clean: true
  },
  devServer: {
    port: 3010,
    open: true,
    proxy: {
      "/api": "http://localhost:8080",
      "/images": "http://localhost:8080",
    },
    historyApiFallback: true
  },
  plugins: [
    new webpack.DefinePlugin({
      __CONFIG__: JSON.stringify(config)
    }),
  ]
});
