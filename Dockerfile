FROM maven:3.8.3-openjdk-17 AS MAVEN_BUILD
WORKDIR /interop-be-signal/
COPY . .
RUN mvn -q clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=MAVEN_BUILD /interop-be-signal/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]