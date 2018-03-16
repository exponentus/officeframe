'use strict';

const path = require('path');
const helpers = require('./helpers');

const CommonsChunkPlugin = require('webpack/lib/optimize/CommonsChunkPlugin');
const ContextReplacementPlugin = require('webpack/lib/ContextReplacementPlugin');
const DefinePlugin = require('webpack/lib/DefinePlugin');
const LoaderOptionsPlugin = require('webpack/lib/LoaderOptionsPlugin');
const UglifyJsPlugin = require('webpack/lib/optimize/UglifyJsPlugin');

const AngularCompilerPlugin = require('@ngtools/webpack').AngularCompilerPlugin;
const CheckerPlugin = require('awesome-typescript-loader').CheckerPlugin;
const CompressionPlugin = require('compression-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');

/**
 * Webpack Constants
 */
const HMR = helpers.hasNpmFlag('hot');
const AOT = helpers.hasNpmFlag('aot');
const METADATA = {
    title: '',
    baseUrl: '/',
    isDevServer: false, // helpers.isWebpackDevServer(),
    HMR: HMR
};

// Plugins
var plugins = [
    new ExtractTextPlugin('[name].css', {
        allChunks: true
    }),

    new CheckerPlugin(),

    // Объявим переменную для использования в переменной окружения environment.ts
    new DefinePlugin({
        ENVIRONMENT: JSON.stringify({
            production: AOT,
            hmr: HMR
        })
    }),

    /**
     * This enables tree shaking of the vendor modules
     */
    new CommonsChunkPlugin({
        name: 'vendor',
        chunks: ['app'],
        minChunks: module => /node_modules\//.test(module.resource)
    }),

    new ContextReplacementPlugin(
        // The (\\|\/) piece accounts for path separators in *nix and Windows
        /angular(\\|\/)core(\\|\/)(esm(\\|\/)src|src)(\\|\/)linker/,
        helpers.root('') // location of your src
    ),

    new ContextReplacementPlugin(/moment[\\/]locale$/, /^\.\/(ru)$/),

    new CompressionPlugin({
        // В output['filename', 'chunkFilename'] при aot для чанков указали расширение файлов .js.gz
        // оставим имена файлов не изменно для aot
        asset: AOT ? '[path]' : '[path].gz[query]',
        algorithm: 'gzip',
        regExp: AOT ? /\.gz$/ : /\.js$|\.map$/,
        threshold: 1500
    }),

    new LoaderOptionsPlugin({
        minimize: true,
        debug: false
    }),

    new AngularCompilerPlugin({
        disabled: !AOT,
        skipCodeGeneration: false,
        tsConfigPath: helpers.root('tsconfig.aot.json'),
        entryModule: helpers.root('src/app/app.module#AppModule'),
        mainPath: helpers.root('src/main.browser.ts'),
        typeChecking: false,
        compilerOptions: {
            noEmit: false,
            noEmitHelpers: false
        }
    })
];

if (AOT) {
    plugins.push(new UglifyJsPlugin({
        test: /\.js\.gz$/,
        beautify: false, //prod
        output: {
            comments: false
        }, //prod
        mangle: {
            screw_ie8: true
        }, //prod
        compress: {
            screw_ie8: true,
            warnings: false,
            conditionals: true,
            unused: true,
            comparisons: true,
            sequences: true,
            dead_code: true,
            evaluate: true,
            if_return: true,
            join_vars: true,
            negate_iife: false // we need this for lazy v8
        }
    }));
}

/**
 * Webpack configuration
 */
module.exports = function(options) {
    return {
        devtool: 'source-map',

        entry: {
            'vendor': helpers.root('src/polyfills.browser.ts'),
            'app': helpers.root('src/main.browser.ts')
        },

        output: {
            path: helpers.root('../assets/dist'),
            publicPath: 'assets/dist/',
            // Файлы именуем с расширением gz, что бы при ленивой загрузке, грузились gz файлы
            // Смотреть CompressionPlugin в plugins.aot. Сжимаем файлы с расширением .js.gz, не меняя имени.
            filename: AOT ? '[name].js.gz' : '[name].js',
            chunkFilename: AOT ? '[id].chunk.js.gz' : '[id].chunk.js'
        },

        resolve: {
            extensions: ['.ts', '.js', '.json'],
            modules: [
                helpers.root('src'),
                helpers.root('node_modules')
            ]
        },

        module: {
            rules: [{
                test: /(?:\.ngfactory\.js|\.ngstyle\.js|\.ts)$/,
                loader: '@ngtools/webpack'
            }, {
                test: /\.css$/,
                exclude: helpers.root('src', 'app'),
                loader: ExtractTextPlugin.extract('style-loader', 'css-loader?-autoprefixer')
            }, {
                test: /\.css$/,
                include: helpers.root('src', 'app'),
                loader: 'raw-loader'
            }, {
                test: /\.html$/,
                loader: 'html-loader?caseSensitive=true&removeAttributeQuotes=false&minimize=true&conservativeCollapse=false',
                exclude: [helpers.root('index.html')]
            }, {
                test: /\.js$/,
                exclude: /node_modules/,
                loader: 'babel-loader'
            }, {
                test: /\.(jpg|png|gif)$/,
                use: 'file-loader'
            }, {
                test: /\.(eot|woff2?|svg|ttf)([\?]?.*)$/,
                use: 'file-loader'
            }],
            noParse: [/zone\.js\/dist\/.+/]
        },

        plugins: plugins,

        stats: {
            colors: true
        },

        node: {
            global: true,
            crypto: 'empty',
            process: true,
            module: false,
            clearImmediate: false,
            setImmediate: false
        }
    };
}
