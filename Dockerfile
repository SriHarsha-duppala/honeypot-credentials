# ===== Stage 1: Build React frontend =====
FROM node:18-alpine AS frontend-build

WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend ./
RUN npm run build

# ===== Stage 2: Build Spring Boot backend =====
FROM eclipse-temurin:17-jdk AS backend-build

WORKDIR /app
COPY backend/pom.xml .
RUN ./mvnw dependency:go-offline || true
COPY backend ./
COPY --from=frontend-build /app/frontend/dist ./src/main/resources/static

RUN ./mvnw clean package -DskipTests

# ===== Stage 3: Runtime image =====
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar

# Set environment variables
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
