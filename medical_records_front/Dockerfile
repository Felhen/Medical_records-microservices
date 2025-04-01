# Use Node.js base image
FROM node:18-alpine

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the entire frontend codebase
COPY . .

# Expose React dev server port
EXPOSE 3000

# Start React app in dev mode (adjust if needed)
CMD ["npm", "start"]
