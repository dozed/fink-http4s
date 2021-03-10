const webpack = require("webpack");
const path = require("path");
const { merge } = require("webpack-merge");
const common = require("./webpack.common.js");

const config = {
  publicPath: "/"
};

module.exports = merge(common, {
  mode: "production",
  output: {
    path: path.join(__dirname, "dist"),
    publicPath: config.publicPath,
    filename: "bundle.js",
    clean: true
  },
  plugins: [
    new webpack.DefinePlugin({
      __CONFIG__: JSON.stringify(config)
    })
  ]
});
