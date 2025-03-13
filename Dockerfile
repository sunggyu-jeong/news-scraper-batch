# tzdata 패키지 설치 및 타임존 설정(Asia/Seoul)
RUN apt update && apt install -y tzdata

# 컨테이너의 TZ 환경변수 값 설정
ENV TZ=Asia/Seoul

# JDK Gradle 이미지 사용
FROM gradle:8-jdk17 AS builder

# 컨테이너 내 작업 디렉토리 /app으로 지정
WORKDIR /app

# 프로젝트 보사
COPY . .

# gradle을 이용하여 프로젝트 빌드 > snapshot 생성
RUN gradle build -x test

# 실행용 컨테이너
FROM eclipse-temurin:17-jdk

WORKDIR /app

# 빌드된 JAR 파일을 복사
COPY --from=builder /app/build/libs/news-scraper-batch-0.0.1-SNAPSHOT.jar app.jar

# 컨테이너가 실행될 때 Java JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]