FROM maven:3.9.7-eclipse-temurin-21 AS builder
WORKDIR /workspace/app
COPY pom.xml ../pom.xml
COPY citas-service/pom.xml ./pom.xml
COPY citas-service/src ./src
RUN mvn -B -DskipTests package spring-boot:repackage

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/app/target/citas-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
