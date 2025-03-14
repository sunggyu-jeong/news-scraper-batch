package com.sunggyu.news_scraper_batch

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@MapperScan("com.sunggyu.news_scraper_batch.batch.mapper")
class NewsScraperBatchApplication

fun main(args: Array<String>) {
	runApplication<NewsScraperBatchApplication>(*args)
}
