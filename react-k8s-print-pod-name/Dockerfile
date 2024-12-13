# Build stage
FROM node:20 AS build

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json (or yarn.lock)
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application files
COPY . .

# Build the Vite.js project
RUN npm run build

# Prepare stage for runtime environment (Apache HTTPD 2.4)
FROM httpd:2.4

# Copy the built files from the build stage to the httpd server
COPY --from=build /app/dist/ /usr/local/apache2/htdocs/

# Copy the entrypoint script and make it executable
COPY ./entrypoint.sh /usr/local/bin/entrypoint.sh

RUN chmod +x /usr/local/bin/entrypoint.sh

# Expose port 80 to serve the application
EXPOSE 80

# Set entrypoint to execute the script during container start-up
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
