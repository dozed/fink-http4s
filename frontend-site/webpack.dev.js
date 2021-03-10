const path = require("path");
const { merge } = require("webpack-merge");
const common = require("./webpack.common.js");

module.exports = merge(common, {
  mode: "development",
  output: {
    path: path.join(__dirname, "dist"),
    publicPath: "/",
    filename: "bundle.js",
    clean: true
  },
  devServer: {
    port: 3010,
    open: true,
    proxy: {
      "/api": "http://localhost:8080"
    },
    historyApiFallback: true
  },
});
