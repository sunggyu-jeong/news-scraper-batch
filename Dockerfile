FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY build/libs/news-scraper-batch-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]