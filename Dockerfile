# Stage 1: Gradle 빌드를 위한 단계
FROM gradle:7.6-jdk17 AS build
WORKDIR /app
# 모든 소스 코드를 복사하여 빌드 환경 구성
COPY . .
# gradlew를 이용해 빌드 실행 (snapshot JAR 생성)
# 환경변수는 테스트과정에서 실행될 수 없으므로 테스트는 생략한다.
RUN ./gradlew build -x test

# Stage 2: 실행 환경 설정
FROM eclipse-temurin:17-jdk
WORKDIR /app

# tzdata 패키지 설치 및 타임존 설정(Asia/Seoul)
RUN apt update && apt install -y tzdata
ENV TZ=Asia/Seoul

# 첫 번째 단계에서 생성한 JAR 파일을 복사
COPY --from=build /app/build/libs/news-scraper-batch-0.0.1-SNAPSHOT.jar app.jar
# 컨테이너 실행 시 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]