package com.sunggyu.news_scraper_batch.batch.scheduler

import com.sunggyu.news_scraper_batch.batch.service.ApiService
import com.sunggyu.news_scraper_batch.batch.service.EmailService
import com.sunggyu.news_scraper_batch.batch.service.ExcelService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class BatchScheduler(
    private val apiService: ApiService,
    private val excelService: ExcelService,
    private val emailService: EmailService
) {
    @Scheduled(cron = "0 0 9 ? * MON-FRI")
    fun runBatchJob() {

    }
}