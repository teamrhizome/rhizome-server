# 빌드 단계
FROM gradle:jdk21-jammy AS builder
WORKDIR /build

COPY gradle ./gradle
COPY build.gradle.kts ./
COPY settings.gradle.kts ./
RUN gradle dependencies --no-daemon --info

COPY src ./src
RUN gradle build --no-daemon --info

# 실행 단계
FROM eclipse-temurin:21-jammy
WORKDIR /app

COPY --from=builder /build/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]