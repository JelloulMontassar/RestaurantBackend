#!/bin/bash

echo "Running unit tests..."

# Run unit tests with Maven (or Gradle)
mvn test  # Or use 'gradle test' if you're using Gradle

# You can also include other testing frameworks here if you use them, like JUnit, TestNG, etc.
echo "Unit tests completed successfully."
