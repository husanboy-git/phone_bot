# 1. 빌드 환경 설정
FROM openjdk:23-jdk-slim AS build

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 파일 복사
COPY . .

# gradlew에 실행 권한 추가
RUN chmod +x gradlew

# Gradle 빌드 (테스트는 제외)
RUN ./gradlew clean build -x test

# 2. 실행 환경 설정
FROM openjdk:23-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 환경 변수 설정
ENV MYSQLDATABASE=railway
ENV MYSQLHOST=mysql.railway.internal
ENV MYSQLPORT=3306
ENV MYSQLUSER=root
ENV MYSQLPASSWORD=teAFSLvxGQxVVKljSibRKScjxphPbxFR
ENV TELEGRAM_BOT_TOKEN=8163853097:AAGpjtmupYG4rjeCNcV5xq5z4e2ltTCBKeU
ENV ADMIN_PASSWORD=Java2023

# Health Check (애플리케이션의 상태를 확인하는 URL로 변경 가능)
HEALTHCHECK CMD curl --fail http://localhost:8080/actuator/health || exit 1

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
