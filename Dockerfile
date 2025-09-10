FROM gradle:8.4.0-jdk21 AS build
COPY . /app
WORKDIR /app

# Etapa de execução
FROM openjdk:21-jdk-slim
COPY --from=build /app/build/libs/cooperfilme-0.0.1-SNAPSHOT.jar /app/cooperfilme-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/cooperfilme-0.0.1-SNAPSHOT.jar"]