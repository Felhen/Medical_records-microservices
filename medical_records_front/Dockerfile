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

# Build the React app for production
RUN npm run build

# Install a lightweight static server to serve the build folder
RUN npm install -g serve

# Expose port 3000 (CBH will use this)
EXPOSE 3000

# Start the static server
CMD ["serve", "-s", "build", "-l", "3000"]
