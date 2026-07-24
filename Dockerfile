# ---------- Stage 1 : Build ----------
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Copy Maven Wrapper
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build Spring Boot project
RUN ./mvnw clean package -DskipTests


# ---------- Stage 2 : Run ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy generated JAR
COPY --from=builder /app/target/financechatbot-0.0.1-SNAPSHOT.jar app.jar

# Render provides PORT dynamically
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
