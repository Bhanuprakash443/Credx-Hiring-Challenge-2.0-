# Stage 1: Build the Angular frontend
FROM node:20-alpine AS frontend-builder
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ ./
RUN npm run build

# Stage 2: Build the Spring Boot backend
FROM gradle:8.7-jdk21 AS backend-builder
USER root
WORKDIR /app
# Copy the backend source files first
COPY backend /app/backend
# Copy the compiled Angular static assets from Stage 1 into the backend resources folder
COPY --from=frontend-builder /app/backend/src/main/resources/static /app/backend/src/main/resources/static
WORKDIR /app/backend
RUN tr -d '\r' < gradlew > gradlew.tmp && mv gradlew.tmp gradlew && chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

# Stage 3: Run the Spring Boot application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=backend-builder /app/backend/build_recruitment/libs/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
