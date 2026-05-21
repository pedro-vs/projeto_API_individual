FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw \
    && ./mvnw -B -Dmaven.wagon.http.retryHandler.count=5 dependency:go-offline

COPY src ./src
RUN ./mvnw -B -Dmaven.wagon.http.retryHandler.count=5 package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
