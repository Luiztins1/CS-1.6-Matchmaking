# ==========================================
# Estágio 1: Build (Compilação)
# ==========================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /workspace

# 1. Otimização de Cache: Baixa as dependências ANTES do código fonte
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copia o código e faz o build (só executa se o código fonte mudar)
COPY src ./src
RUN mvn -B -DskipTests clean package

# ==========================================
# Estágio 2: Runner (Execução)
# ==========================================
FROM eclipse-temurin:21-jre-alpine

# 3. Segurança: Cria usuário não-root (sintaxe adaptada para Alpine)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# 4. Otimização de Camada: Copia e altera o dono no mesmo comando
COPY --from=builder --chown=appuser:appgroup /workspace/target/*.jar /app/app.jar

USER appuser
EXPOSE 8080

# 5. O JVM identifica essa variável automaticamente
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# 6. Execução direta (PID 1) para Graceful Shutdown
ENTRYPOINT ["java", "-jar", "/app/app.jar"]