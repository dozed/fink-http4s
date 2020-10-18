const HtmlWebpackPlugin = require("html-webpack-plugin");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");

module.exports = {
  devServer: {
    port: 3010,
    open: true,
    proxy: {
      "/api": "http://localhost:8080"
    },
    historyApiFallback: true
  },
  resolve: {
    extensions: ["*", ".js", ".jsx"],
    modules: [ "node_modules", "stylesheets", "src", "../frontend-shared" ]
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
    new CleanWebpackPlugin(),
    new HtmlWebpackPlugin({
      template: "./public/index.html",
      favicon: "./public/favicon.ico",
      filename: "./index.html"
    }),
  ]
};
