# Use the official OpenJDK 17 as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the maven executable to the image
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Create a smaller runtime image
FROM openjdk:17-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built jar from the first stage
COPY --from=0 /app/target/*.jar app.jar

# Make port 8123 available to the world outside this container
EXPOSE 8123

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
