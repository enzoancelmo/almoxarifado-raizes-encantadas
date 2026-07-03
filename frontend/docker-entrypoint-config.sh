#!/bin/sh
set -eu
API_URL="$(printenv API_URL || echo http://localhost:8080)"
printf 'window.__env = { apiUrl: "%s" };
' "$API_URL" > /usr/share/nginx/html/config.js
