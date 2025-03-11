package com.sunggyu.news_scraper_batch.batch.scheduler

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.JobOperator
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BatchScheduler(
    private val newsBatchJob: Job,
    private val publicBatchJob: Job,
    private val jobLauncher: JobLauncher,
    private val jobOperator: JobOperator
) {
    private val logger: Logger = LoggerFactory.getLogger(BatchScheduler::class.java)

    suspend fun executeJob(jobName: String, job: Job, startMessage: String, endMessage: String) {
        val runningExecutions = jobOperator.getRunningExecutions(jobName)
        if (runningExecutions.isNotEmpty()) {
            runningExecutions.forEach { executionId ->
                try {
                    jobOperator.abandon(executionId)
                    logger.info("배치 JOB 강제종료")
                } catch (e: Exception) {
                    logger.error("배치 JOB 강제종료 실패 ${e.message}")
                }
            }
            withContext(Dispatchers.IO) {
                Thread.sleep(10000)
            }
        }
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> $startMessage: ${LocalDateTime.now()}")
        jobLauncher.run(job, org.springframework.batch.core.JobParameters())
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> $endMessage: ${LocalDateTime.now()}")
    }

    @Scheduled(cron = "0 30 9 ? * MON-FRI")
    fun runBatchJob() = runBlocking {
        executeJob("newsBatchJob", newsBatchJob, "뉴스 배치 실행 시작", "뉴스 배치 실행 완료")
    }

    @Scheduled(cron = "0 0 0 1 * *")
    fun runBatchHolidayJob() = runBlocking {
        executeJob("publicBatchJob", publicBatchJob, "공공API 배치 실행 시작", "공공API 배치 실행 완료")
    }
}