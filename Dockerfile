FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /src
COPY . .
RUN mvn -q -DskipTests package

FROM payara/server-full:6.2024.12-jdk17
COPY --from=build /src/target/*.war $DEPLOY_DIR/antons-skafferi.war
