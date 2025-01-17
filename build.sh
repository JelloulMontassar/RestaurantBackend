#!/bin/bash

# Set any environment variables, if necessary
# export MY_VAR=value

echo "Starting build process..."

# Assuming you're using Maven to build your Spring Boot app (adjust according to your build tool)
# If you're using Gradle, replace 'mvn' with 'gradle'

# Clean and compile the app
mvn clean install -DskipTests  # Skipping tests to speed up the build process (tests are handled separately)

# If you're using Docker, you can also build the Docker image here (optional)
# docker build -t myapp .

echo "Build process completed successfully."
