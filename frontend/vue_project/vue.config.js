module.exports = {
  devServer: {
    proxy: 'https://localhost:8090/',
  },
  resolve: {fallback: { "crypto": require.resolve("crypto-browserify") }}
};