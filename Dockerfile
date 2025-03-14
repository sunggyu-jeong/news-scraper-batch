FROM eclipse-temurin:17-jdk

# tzdata 패키지 설치 및 타임존 설정(Asia/Seoul)
RUN apt update && apt install -y tzdata

# 컨테이너의 TZ 환경변수 값 설정
ENV TZ=Asia/Seoul

# 컨테이너 내 작업 디렉토리 /app으로 지정
WORKDIR /app

# 로컬 빌드된 JAR 파일을 /app/app.jar 로 복사
COPY build/libs/news-scraper-batch-0.0.1-SNAPSHOT.jar app.jar

# 컨테이너가 실행될 때 Java JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]