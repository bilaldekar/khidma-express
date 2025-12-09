# Use Java 21 base image
#FROM eclipse-temurin:21-jdk
#
#WORKDIR /app
#
## Copy Maven wrapper + project descriptor
#COPY mvnw ./
#COPY .mvn .mvn
#COPY pom.xml ./
#
#RUN chmod +x mvnw
#
## Build directly (no go-offline)
#COPY src src
#RUN ./mvnw clean package -DskipTests
#
#EXPOSE 8080
#CMD ["java", "-jar", "target/khidma-express-0.0.1-SNAPSHOT.jar"]

# -------------------------
# 1. Build stage
# -------------------------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Maven files first to enable dependency caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw

# Download dependencies (cached by Docker layer)
RUN ./mvnw dependency:go-offline -B

# Now copy the actual code
COPY src src

# Build the app
RUN ./mvnw clean package -DskipTests -B


# -------------------------
# 2. Runtime stage
# -------------------------
FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
