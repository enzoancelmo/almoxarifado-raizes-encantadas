FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S estoque && adduser -S estoque -G estoque
COPY --from=build /app/target/backend-*.jar app.jar
USER estoque
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
