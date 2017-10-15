const path = require('path');
const paths = {
    MAIN: path.resolve('scalatra-react-web', 'src', 'main'),
    NODE_MODULES: path.resolve('scalatra-react-web', 'src', 'main', 'node', 'node_modules')
}

module.exports = {
    entry: path.join(paths.MAIN, 'frontend', 'main.jsx'),
    output: {
        path: path.join(paths.MAIN, 'webapp', 'js'),
        filename: 'bundle.js',
        library: ['com', 'nudemeth', 'example', 'web']
    },
    module: {
        loaders: [{
            test: /\.jsx$/,
            loader: 'babel-loader',
            exclude: /node_modules/,
            query: {
                presets: [
                    path.join(paths.NODE_MODULES, 'babel-preset-es2015'),
                    path.join(paths.NODE_MODULES, 'babel-preset-stage-2'),
                    path.join(paths.NODE_MODULES, 'babel-preset-react')
                ],
                plugins: [
                    path.join(paths.NODE_MODULES, 'babel-plugin-transform-runtime')
                ]
            }
        }]
    },
    resolve: {
        modules: [paths.NODE_MODULES]
    },
    resolveLoader: {
        modules: [paths.NODE_MODULES]
    }
}