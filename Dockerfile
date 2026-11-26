# Use an official Maven image as the base image
FROM maven:3.9.5-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files
COPY pom.xml ./
COPY src ./src

# Run Maven build to download dependencies and package the application
RUN mvn clean package -DskipTests

# Stage 2: Final image for running the application
FROM eclipse-temurin:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged application from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Add Google Java Format tool (optional integration for formatting)
ADD https://github.com/google/google-java-format/releases/download/v1.17.0/google-java-format-1.17.0-all-deps.jar /usr/local/bin/google-java-format.jar

# Make Google Java Format executable
RUN chmod +x /usr/local/bin/google-java-format.jar

# Expose the application port
EXPOSE 8080

# Set the entry point for running the application
ENTRYPOINT ["java", "-jar", "app.jar"]
