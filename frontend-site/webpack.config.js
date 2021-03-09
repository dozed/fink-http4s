const path = require("path");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const CopyPlugin = require("copy-webpack-plugin");

const outputDirectory = "dist";

module.exports = {
  output: {
    path: path.join(__dirname, outputDirectory),
    publicPath: "/",
    filename: "bundle.js",
    clean: true
  },
  resolve: {
    extensions: ["*", ".js", ".jsx"],
    modules: [ "node_modules", "stylesheets", "src", "../frontend-shared" ]
  },
  devServer: {
    port: 3010,
    open: true,
    proxy: {
      "/api": "http://localhost:8080"
    },
    historyApiFallback: true
  },
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader",
        },
      },
      {
        test: /(\.css|\.scss)$/,
        use: [
          "style-loader",
          "css-loader",
          "sass-loader"
        ]
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: "./public/index.html",
      favicon: "./public/favicon.ico",
      filename: "./index.html"
    }),
    new CopyPlugin({
      patterns: [
        { from: "../data/uploads", to: "data/uploads" },
      ]
    }),
  ]
};
