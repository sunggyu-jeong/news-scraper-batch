# ![Image](https://github.com/user-attachments/assets/cbe42540-59af-4e8d-8f59-eb22f0159870)

**Spring Boot** 기반 배치 애플리케이션으로, **Spring Batch**, **Kotlin**, 그리고 **PostgreSQL** 데이터베이스를 사용하여 뉴스 스크래핑 및 공휴일 데이터 처리

## 기술 스택

- **Spring Boot**: 애플리케이션 설정 및 실행을 담당하는 프레임워크
- **Spring Batch**: 대용량 데이터 처리 및 배치 작업 수행을 위한 프레임워크
- **Kotlin**: 간결하고 현대적인 JVM 언어
- **PostgreSQL**: 안정적인 RDBMS 기반 데이터베이스

## 프로젝트 기능

뉴스 데이터 스크래핑과 공휴일 정보 수집 등 다양한 배치 작업을 정해진 스케줄에 따라 실행한다. 

### 주요 기능:

- 뉴스 데이터 스크래핑 및 담당자 메일 전송
- 공휴일 정보 수집 및 데이터베이스 저장 (FetchHolidayTasklet)
- 스케줄링을 통한 자동 배치 작업 실행
- 재시도 로직을 통한 안정적인 작업 수행
- 공휴일, 주말에는 전송하지 않게 작업

## 시작하기

### 요구 사항

- JDK 11 이상
- Kotlin
- Maven 또는 Gradle
- PostgreSQL

### 설치 및 빌드

```bash
# 프로젝트 클론
git clone <https://github.com/your-repo/news-scraper-batch.git>
cd news-scraper-batch

# Maven을 사용하는 경우
mvn clean install

# Gradle을 사용하는 경우
./gradlew build

# 환경변수 설정
NEWS_SCRAPER_BATCH_BASE_URL: 웹서버 URL
NEWS_SCRAPER_BATCH_PUBLIC_URL: 공공 API URL
SERVICEKEY: 공공API 서비스 키
MAIL_HOST: SMTP host
MAIL_PORT: STMP port
MAIL_USERNAME: 자동으로 메일을 전송하려는 사용자 이메일 주소
MAIL_PASSWORD: 자동으로 메일을 전송하려는 사용자의 비밀번호
USERNAME:클라이언트 사용자 ID
PASSWORD:클라이언트 사용자 비밀번호
DATABASE_URL: 데이터베이스 주소
POSTGRES_USER: 데이터베이스 사용자명(internal)
POSTGRES_PASSWORD: 데이터베이스 사용자 비밀번호(internal)
```
