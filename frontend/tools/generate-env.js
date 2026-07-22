const fs = require('fs');
const path = require('path');

const apiUrl = process.env.FRONTEND_API_URL || process.env.API_URL || '/api';
const output = `window.__APP_CONFIG__ = {\n  apiUrl: ${JSON.stringify(apiUrl)}\n};\n`;
const target = path.join(__dirname, '..', 'public', 'env.js');

fs.writeFileSync(target, output, 'utf8');
console.log(`Frontend API URL configured as: ${apiUrl}`);
