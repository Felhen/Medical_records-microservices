# Use a more complete image with Python, build tools, etc.
FROM node:18-bullseye

# Set working directory
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN apt-get update \
  && apt-get install -y python3 make g++ \
  && npm install

# Copy the rest of the app
COPY . .

# Expose port and start
EXPOSE 5000
CMD ["node", "server.js"]
