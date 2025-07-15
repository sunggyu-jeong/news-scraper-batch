# Stage 1: Gradle Build Stage
FROM gradle:7.6-jdk17 AS build
WORKDIR /app

# 1) 캐시 활용: 빌드 스크립트만 먼저 복사
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle gradle/
RUN chmod +x gradlew

# 2) 의존성만 먼저 다운로드 (캐시 레이어)
RUN ./gradlew clean dependencies --no-daemon --configure-on-demand

# 3) 전체 소스 복사 후 빌드 (테스트 제외)
COPY . .
RUN ./gradlew clean bootJar -x test --no-daemon --configure-on-demand

# Stage 2: Runtime Stage
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 비대화형 모드로 tzdata 설치 및 타임존 설정
ARG DEBIAN_FRONTEND=noninteractive
ENV DEBIAN_FRONTEND=${DEBIAN_FRONTEND} TZ=Asia/Seoul
RUN apt-get update \
 && apt-get install -y --no-install-recommends tzdata \
 && ln -fs /usr/share/zoneinfo/$TZ /etc/localtime \
 && dpkg-reconfigure --frontend noninteractive tzdata \
 && apt-get clean \
 && rm -rf /var/lib/apt/lists/*

EXPOSE 4000

# 빌드 아티팩트 복사
COPY --from=build /app/build/libs/news-scraper-batch-0.0.1-SNAPSHOT.jar app.jar

# 컨테이너 시작 시 실행
ENTRYPOINT ["java", "-Dserver.port=4000", "-jar", "app.jar"]