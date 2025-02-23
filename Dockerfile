# Base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the build artifacts
COPY build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]