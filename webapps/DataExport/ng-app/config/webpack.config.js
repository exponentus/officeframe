'use strict';

const helpers = require('./helpers');
const { resolve } = require('path');

const rxPaths = require('rxjs/_esm5/path-mapping');
const ContextReplacementPlugin = require('webpack/lib/ContextReplacementPlugin');
const DefinePlugin = require('webpack/lib/DefinePlugin');
const ProgressPlugin = require('webpack/lib/ProgressPlugin');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const CompressionPlugin = require('compression-webpack-plugin');
const CircularDependencyPlugin = require('circular-dependency-plugin');
const { CleanCssWebpackPlugin } = require('@angular-devkit/build-angular/src/angular-cli-files/plugins/cleancss-webpack-plugin');
const { SuppressExtractedTextChunksWebpackPlugin } = require('@angular-devkit/build-angular/src/angular-cli-files/plugins/suppress-entry-chunks-webpack-plugin');
const { AngularCompilerPlugin } = require('@ngtools/webpack');

// Constants
const HMR = helpers.hasNpmFlag('hot');
const AOT = helpers.hasNpmFlag('aot');

let ENV_FILE,
    MODE;
if (AOT) {
    MODE = 'production';
    ENV_FILE = resolve('src/environments/environment.ts');
} else {
    MODE = 'development';
    ENV_FILE = resolve('src/environments/environment.ts');
}

// Plugins
var plugins = [
    new ProgressPlugin(),

    // Объявим переменную для использования в переменной окружения environment.ts
    new DefinePlugin({
        ENVIRONMENT: JSON.stringify({
            production: AOT,
            hmr: HMR
        })
    }),

    new MiniCssExtractPlugin({
        filename: '[name].css'
    }),

    new SuppressExtractedTextChunksWebpackPlugin(),

    new CompressionPlugin({
        // В output['filename', 'chunkFilename'] при aot для чанков указали расширение файлов .js.gz
        // оставим имена файлов не изменно для aot
        asset: AOT ? '[path]' : '[path].gz[query]',
        algorithm: 'gzip',
        regExp: AOT ? /\.gz$/ : /\.js$|\.map$/,
        threshold: 1500
    }),

    new AngularCompilerPlugin({
        disabled: !AOT,
        skipCodeGeneration: false,
        tsConfigPath: resolve('tsconfig.aot.json'),
        entryModule: resolve('src/app/app.module#AppModule'),
        mainPath: resolve('src/main.browser.ts'),
        nameLazyFiles: false,
        sourceMap: false,
        typeChecking: false,
        compilerOptions: {
            noEmit: false,
            noEmitHelpers: false
        },
        hostReplacementPaths: {
            [resolve('src/environments/environment.ts')]: ENV_FILE
        }
    }),

    new CircularDependencyPlugin({
        exclude: /[\\\/]node_modules[\\\/]/
    }),

    new ContextReplacementPlugin(/moment[/\\]locale$/, /en-us|ru|kk|es|pt/)
];

/**
 * Webpack configuration
 */
module.exports = function(options) {
    return {
        mode: MODE,

        devtool: 'source-map',

        entry: {
            app: resolve('src/main.browser.ts'),
            vendor: resolve('src/polyfills.browser.ts')
        },

        output: {
            path: resolve('../assets/dist'),
            publicPath: 'assets/dist/',
            // Файлы именуем с расширением gz, что бы при ленивой загрузке, грузились gz файлы
            // Смотреть CompressionPlugin в plugins.aot. Сжимаем файлы с расширением .js.gz, не меняя имени.
            filename: AOT ? '[name].js.gz' : '[name].js',
            chunkFilename: AOT ? '[id].chunk.js.gz' : '[id].chunk.js'
        },

        resolve: {
            alias: {
                ...rxPaths(),
                '@nb/*': './@nb-src-tmp/*'
            },
            extensions: ['.ts', '.js', '.json']
        },

        node: false,

        performance: {
            hints: false,
        },

        module: {
            rules: [{
                test: /\.ts$/,
                use: '@ngtools/webpack'
            }, {
                test: /\.js$/,
                loader: '@angular-devkit/build-optimizer/webpack-loader',
                options: { sourceMap: false }
            }, {
                test: /\.js$/,
                exclude: /(ngfactory|ngstyle).js$/,
                enforce: 'pre',
                use: 'source-map-loader'
            }, {
                test: /\.css$/,
                exclude: resolve('src', 'app'),
                loader: ['to-string-loader', 'css-loader']
            }, {
                test: /\.css$/,
                include: resolve('src', 'app'),
                loader: 'raw-loader'
            }, {
                test: /\.html$/,
                exclude: [resolve('index.html')],
                loader: 'html-loader?caseSensitive=true&removeAttributeQuotes=false&minimize=true&conservativeCollapse=false'
            }, {
                test: /\.(jpg|png|gif)$/,
                use: 'file-loader'
            }, {
                test: /\.(eot|woff2?|svg|ttf)([\?]?.*)$/,
                use: 'file-loader'
            }, {
                // This hides some deprecation warnings that Webpack throws
                test: /[\/\\]@angular[\/\\]core[\/\\].+\.js$/,
                parser: { system: true },
            }]
        },

        optimization: {
            noEmitOnErrors: true,
            splitChunks: {
                cacheGroups: {
                    default: {
                        chunks: 'async',
                        minChunks: 2,
                        priority: 10
                    },
                    common: {
                        name: 'common',
                        chunks: 'async',
                        minChunks: 2,
                        enforce: true,
                        priority: 5
                    },
                    vendors: false,
                    vendor: false
                }
            },
            minimizer: [
                new UglifyJsPlugin({
                    test: /\.js\.gz$/,
                    cache: true,
                    parallel: true,
                    uglifyOptions: {
                        beautify: false,
                        output: {
                            ascii_only: true,
                            comments: false,
                            webkit: true,
                        },
                        compress: {
                            pure_getters: true,
                            passes: 3,
                            inline: 3,
                        }
                    }
                }),
                new CleanCssWebpackPlugin({
                    sourceMap: false,
                    test: (file) => /\.(?:css)$/.test(file),
                })
            ]
        },

        plugins: plugins
    };
}
