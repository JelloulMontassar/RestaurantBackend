#!/bin/bash

echo "Running integration tests..."

# Run integration tests (this can be adjusted based on your test framework)
./mvnw verify  # For Maven-based projects, replace with your own test command if different

echo "Integration tests completed."
