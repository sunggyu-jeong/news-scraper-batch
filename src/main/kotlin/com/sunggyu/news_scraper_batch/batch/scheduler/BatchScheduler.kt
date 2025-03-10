package com.sunggyu.news_scraper_batch.batch.scheduler

import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BatchScheduler(
    private val newsBatchJob: Job,
    private val publicBatchJob: Job,
    private val jobLauncher: JobLauncher
) {
    private val logger: Logger = LoggerFactory.getLogger(BatchScheduler::class.java)

    @Scheduled(cron = "0 30 9 ? * MON-FRI")
    fun runBatchJob() = runBlocking {
        println(">>>>> 뉴스 배치 실행 시작: ${LocalDateTime.now()}")
        jobLauncher.run(newsBatchJob, org.springframework.batch.core.JobParameters())
        println(">>>>> 뉴스 배치 실행 완료: ${LocalDateTime.now()}")
    }

    @Scheduled(cron = "0 0 0 1 * *")
    fun runBatchHolidayJob() = runBlocking {
        println(">>>>> 공공API 배치 실행 시작: ${LocalDateTime.now()}")
        jobLauncher.run(publicBatchJob, org.springframework.batch.core.JobParameters())
        println(">>>>> 공공API 배치 실행 완료: ${LocalDateTime.now()}")
    }
}