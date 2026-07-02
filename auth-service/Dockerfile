FROM maven:3.9.7-eclipse-temurin-21 AS builder
WORKDIR /workspace/app
COPY pom.xml ../pom.xml
COPY auth-service/pom.xml ./pom.xml
COPY auth-service/src ./src
RUN mvn -B -DskipTests package spring-boot:repackage

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/app/target/auth-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]
