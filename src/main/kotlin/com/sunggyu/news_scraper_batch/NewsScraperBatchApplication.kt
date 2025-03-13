package com.sunggyu.news_scraper_batch

import io.github.cdimascio.dotenv.Dotenv
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@MapperScan("com.sunggyu.news_scraper_batch.batch.mapper")
class NewsScraperBatchApplication

fun main(args: Array<String>) {
	val dotenv = Dotenv.load()
	System.setProperty("DATABASE_URL", dotenv["DATABASE_URL"] ?: "")
	System.setProperty("POSTGRES_USER", dotenv["POSTGRES_USER"] ?: "postgres")
	System.setProperty("POSTGRES_PASSWORD", dotenv["POSTGRES_PASSWORD"] ?: "")

	val context = runApplication<NewsScraperBatchApplication>(*args)
	val env: Environment = context.environment

	// Actuator 설정 값 출력
	val actuatorExposure = env.getProperty("management.endpoints.web.exposure.include")
	println("🔍 Actuator exposure setting: $actuatorExposure")

	// 확인용 추가 로그
	println("✅ Application started successfully.")

	runApplication<NewsScraperBatchApplication>(*args)
}