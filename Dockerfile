# ========================================================
# Stage 1: Build Application (Self-Contained Maven Build)
# ========================================================
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build

RUN apk add --no-cache dos2unix

# Copy Maven wrapper & POM first for layer caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN dos2unix mvnw && chmod +x mvnw

# Download dependencies offline (cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B || true

# Copy source code and build production jar
COPY src/ ./src/
RUN ./mvnw clean package -DskipTests -B

# ========================================================
# Stage 2: Minimal Production JRE Runtime
# ========================================================
FROM eclipse-temurin:21-jre-alpine AS runner
LABEL maintainer="UTYCC Registration System"

# Install curl for healthcheck and tzdata for accurate timezone
RUN apk add --no-cache curl tzdata

# Set timezone default
ENV TZ=Asia/Yangon

WORKDIR /app

# Run as non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Create directories for persistent volumes
RUN mkdir -p /app/uploads/payment \
             /app/uploads/announcements \
             /app/uploads/pre_enrollment_old \
             /app/logs \
    && chown -R appuser:appgroup /app

# Copy JAR from builder stage
COPY --from=builder --chown=appuser:appgroup /build/target/reg-*.jar /app/app.jar

USER appuser

EXPOSE 8080

# Production environment defaults
ENV SERVER_PORT=8080 \
    SPRING_DATASOURCE_URL=jdbc:mariadb://mariadb:3306/student_reg?useUnicode=true&characterEncoding=UTF-8 \
    SPRING_DATASOURCE_USERNAME=student_reg \
    SPRING_DATASOURCE_PASSWORD=utycc2025 \
    FILE_UPLOAD_DIR=/app/uploads \
    ADMIN_UPLOAD_DIR=/app/uploads/payment \
    ADMIN_UPLOAD_BASE=/app/uploads \
    LOGGING_FILE_NAME=/app/logs/app.log \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"

HEALTHCHECK --interval=20s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/auth/ping || exit 1

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
