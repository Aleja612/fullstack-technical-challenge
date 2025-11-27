# -------------------------------------------------------------------
# STAGE 1: Build (Compilación)
# Usamos una imagen de Maven con JDK 17 para compilar el proyecto
# -------------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# 1. Copiamos solo el pom.xml primero para aprovechar el caché de Docker
# Si no cambias dependencias, Docker no volverá a descargar internet entero
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Copiamos el código fuente y compilamos
COPY src ./src
# Compilamos saltando tests (los tests ya debieron pasar en el CI/CD pipeline)
RUN mvn clean package -DskipTests

# -------------------------------------------------------------------
# STAGE 2: Run (Ejecución)
# Usamos una imagen JRE Alpine (muy ligera) para producción
# -------------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiamos solo el JAR compilado desde la etapa anterior
# El *.jar comodín nos evita tener que saber la versión exacta (0.0.1-SNAPSHOT)
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto estándar de Spring Boot
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]