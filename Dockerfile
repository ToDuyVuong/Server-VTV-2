#1. Build the Docker image
#docker build -t server-vtv-image .

#2. Run the Docker container
#docker run -d --name server-vtv-container server-vtv-image



FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/Server-VTV-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8585
CMD ["java", "-jar", "app.jar"]



#Java 22 use code below
##Step 1: Build the application
 #FROM maven:3.9.9-eclipse-temurin-22 AS build
 #
 #WORKDIR /app
 #COPY pom.xml .
 #COPY src ./src
 #
 #RUN mvn clean package -DskipTests
 #
 ##Step 2: Run the application
 #FROM eclipse-temurin:22-jdk
 #
 #WORKDIR /app
 #COPY --from=build /app/target/*.jar app.jar
 #
 #ENTRYPOINT ["java", "-jar", "app.jar"]
 #
 #
 ##Step 3: Run in the terminal
 ## 1. docker build -t service-register:0.0.1 .
 ## 2. docker run -d service-register:0.0.1

