FROM openjdk:17

WORKDIR /app
COPY build/libs/InstagramStoriesMetric-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]