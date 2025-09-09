# Step 1: Start from official Java image
FROM openjdk:17-jdk-slim

# Step 2: Set working directory
WORKDIR /app

# Step 3: Copy built JAR into container
COPY
target/blogging-application-0.0.1-SNAPSHOT.jar;

# Step 4: Expose port
EXPOSE 8080

# Step 5: Run the JAR
ENTRYPOINT ["java","-jar","app.jar"]
