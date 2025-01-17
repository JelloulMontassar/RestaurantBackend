#!/bin/bash

echo "Running integration tests..."

# Run integration tests with Maven (or Gradle)
mvn verify  # Use 'gradle verify' if using Gradle

# Alternatively, you can run specific integration test classes or configurations, e.g.
# mvn -Dtest=MyIntegrationTest verify

echo "Integration tests completed successfully."
