# Build stage
FROM maven:3.8.8-eclipse-temurin-8 AS build
WORKDIR /HomieHotel
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:8-jdk-alpine
VOLUME /tmp
WORKDIR /HomieHotel
# Copy file WAR từ stage build sang
COPY --from=build /HomieHotel/target/*.war app.war

RUN ls -la && ls -la /HomieHotel && cat pom.xml && mvn clean package -DskipTests

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]