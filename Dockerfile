FROM gradle:8.13-jdk21 AS build
WORKDIR /app

# Кэшируем зависимости Gradle
COPY build.gradle.kts settings.gradle.kts ./
RUN gradle dependencies --no-daemon

# Копируем исходный код и собираем без тестов
COPY . .
RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
ENV SPRING_PROFILES_ACTIVE=docker