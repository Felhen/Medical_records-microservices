# Use an appropriate base image with Node.js installed
FROM node:14-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json to install dependencies
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the React app files to the container
COPY . .

# Build the React app (change 'npm run build' to your actual build command)
RUN npm run build

# Expose the port used by the React app (if applicable)
EXPOSE 3000

# Command to start the React app (if serving via Node.js server)
CMD ["npm", "start"]