# Use an OpenJDK image as the base image
FROM openjdk:17-jdk-slim as build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies (to take advantage of Docker cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire source code
COPY src /app/src

# Build the application (create the JAR file)
RUN mvn clean package -DskipTests

# Second stage: Create the runtime image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar /app/backend.jar

# Expose the port the app runs on (default 8080 for Spring Boot)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "backend.jar"]