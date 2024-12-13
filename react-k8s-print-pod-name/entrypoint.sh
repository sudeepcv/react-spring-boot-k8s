#!/bin/sh

# Replace POD_NAME and API_SERVER placeholders in all .js files in the /usr/local/apache2/htdocs/assets/ directory
for file in /usr/local/apache2/htdocs/assets/*.js; do
  sed -i "s|VITE_POD_NAME_PLACEHOLDER|${VITE_POD_NAME}|g" "$file"  # Replace pod name placeholder
  sed -i "s|VITE_API_SERVER_PLACEHOLDER|${VITE_API_SERVER}|g" "$file"  # Replace API server placeholder
done

# Start Apache in the foreground
httpd-foreground
