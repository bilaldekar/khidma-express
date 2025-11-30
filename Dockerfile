# Use Java 21 base image
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy Maven wrapper + project descriptor
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./

RUN chmod +x mvnw

# Build directly (no go-offline)
COPY src src
RUN ./mvnw clean package -DskipTests

EXPOSE 8080
CMD ["java", "-jar", "target/khidma-express-0.0.1-SNAPSHOT.jar"]
