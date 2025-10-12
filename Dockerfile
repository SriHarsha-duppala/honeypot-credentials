# Use official JDK image to build the app
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy Maven files and download dependencies (for faster builds)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

# Copy the project source and build it
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Use a lightweight JRE image to run the app
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
