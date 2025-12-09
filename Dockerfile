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

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw clean package -DskipTests

# -------------------------
# 2. Runtime stage
# -------------------------
FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
