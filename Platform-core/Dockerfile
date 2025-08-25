# Stage 1: Build the application using Maven
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the entire project context
COPY . .

# Build the entire multi-module project, skipping tests
# This will compile all modules and install them in the local Maven repo
RUN mvn -f pom.xml clean install -DskipTests

# Stage 2: Create the final, slim image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the application jar from the build stage
# The jar is located in the target directory of the platform-nhcx module
COPY --from=build /app/platform-nhcx/target/platform-nhcx-1.0.0.jar app.jar

# Expose the application port
EXPOSE 8085

# Set the entrypoint to run the application
ENTRYPOINT ["java","-jar","app.jar"]
