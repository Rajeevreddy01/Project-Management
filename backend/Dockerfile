FROM node:20-alpine AS frontend-build
WORKDIR /app/frontend
COPY frontend/project-management-system-frontend/package.json ./
RUN npm install
COPY frontend/project-management-system-frontend/ ./
RUN npm run build

FROM maven:3.9.8-eclipse-temurin-17 AS backend-build
WORKDIR /app/backend
COPY backend/pom.xml ./
COPY backend/src ./src
COPY --from=frontend-build /app/frontend/dist ./src/main/resources/static
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=backend-build /app/backend/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
