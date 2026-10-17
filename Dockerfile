
# Stage 1: Build
FROM maven:3.9.16-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Baixa as dependências para aproveitar o cache
COPY pom.xml .
RUN mvn dependency:go-offline -B --no-transfer-progress

# Compila a aplicação
COPY src ./src
RUN mvn clean package -DskipTests --no-transfer-progress


# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Cria um usuário sem privilégios de root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copia o JAR gerado pelo Maven
COPY --from=builder --chown=appuser:appgroup /app/target/*.jar app.jar

# Executa a aplicação sem root
USER appuser

EXPOSE 8080

# Verifica a saúde da aplicação
# Requer spring-boot-starter-actuator e wget disponível na imagem
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health | grep -q '"status":"UP"' || exit 1

# Inicia a aplicação
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
