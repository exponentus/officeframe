'use strict'

const fs = require('fs-extra');

try {
    fs.copySync('./node_modules/@nb/core/src', './@nb-src-tmp');
} catch (err) {
    console.error('postinstall', err);
}
