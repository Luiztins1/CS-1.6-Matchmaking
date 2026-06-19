FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /workspace

COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src ./src
RUN ./mvnw -B -DskipTests package
FROM eclipse-temurin:21-jre-jammy

RUN groupadd --system app && useradd --system --gid app --create-home --home-dir /home/app --shell /usr/sbin/nologin app

WORKDIR /app

COPY --from=builder /workspace/target/*.jar /app/app.jar

RUN chown app:app /app/app.jar

USER app

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_TOOL_OPTIONS -jar /app/app.jar"]
