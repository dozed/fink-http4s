const webpack = require("webpack");
const path = require("path");
const { merge } = require("webpack-merge");
const common = require("./webpack.common.js");
const CopyPlugin = require("copy-webpack-plugin");

const config = {
  publicPath: "/"
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
    port: 3000,
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
    new CopyPlugin({
      patterns: [
        { from: "../data/uploads", to: "data/uploads" },
      ]
    })
  ]
});