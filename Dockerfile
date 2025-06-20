# Stage 1: Gradle 빌드를 위한 단계
FROM gradle:7.6-jdk17 AS build
WORKDIR /app
# 캐시 활용용: Gradle Wrapper와 빌드 스크립트만 복사
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle

# 의존성만 미리 다운로드 (Docker layer 캐시)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon --configure-on-demand

# 전체 소스 복사 후 실제 빌드
COPY . .

RUN ./gradlew build -x test --no-daemon --configure-on-demand

# Stage 2: 실행 환경 설정
FROM eclipse-temurin:17-jdk
WORKDIR /app

# tzdata 패키지 설치 및 타임존 설정(Asia/Seoul)
RUN apt update && apt install -y tzdata
ENV TZ=Asia/Seoul

EXPOSE 4000

# 첫 번째 단계에서 생성한 JAR 파일을 복사
COPY --from=build /app/build/libs/news-scraper-batch-0.0.1-SNAPSHOT.jar app.jar
# 컨테이너 실행 시 JAR 파일 실행
ENTRYPOINT ["java", "-Dserver.port=4000", "-jar", "app.jar"]