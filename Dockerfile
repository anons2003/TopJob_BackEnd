# Use JDK 21
FROM openjdk:21-jdk

# Set the working directory
WORKDIR /app

# Copy the application files
COPY . /app

# Build the application
RUN chmod +x ./mvnw && ./mvnw -DoutputFile=target/mvn-dependency-list.log -B -DskipTests clean dependency:list install

# Specify the command to run your application
CMD ["java", "-jar", "target/WebServerSWP-0.0.1-SNAPSHOT.jar"]