'use strict'

const fs = require('fs-extra');

try {
    console.info('postinstall-auto-configure::start');
    fs.copySync('./node_modules/@nb/core/src', './@nb-src-tmp');
    fs.copySync('./node_modules/@nb/core/config/webpack', './config');
    fs.copySync('./node_modules/@nb/core/config/package.json', './package.json');
    fs.copySync('./node_modules/@nb/core/config/keep.gitignore', './.gitignore');
    fs.copySync('./node_modules/@nb/core/config/.jsbeautifyrc', './.jsbeautifyrc');
    fs.copySync('./node_modules/@nb/core/config/auto-configure.js', './auto-configure.js');
    fs.copySync('./node_modules/@nb/core/config/tsconfig.aot.json', './tsconfig.aot.json');
    fs.copySync('./node_modules/@nb/core/config/tsconfig.json', './tsconfig.json');
    fs.copySync('./node_modules/@nb/core/config/tslint.json', './tslint.json');
    console.info('postinstall-auto-configure::end');
} catch (err) {
    console.error('postinstall-auto-configure', err);
}
