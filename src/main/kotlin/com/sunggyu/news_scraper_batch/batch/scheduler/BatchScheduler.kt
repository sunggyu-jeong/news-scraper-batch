package com.sunggyu.news_scraper_batch.batch.scheduler

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
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
                    // 실행 중인 잡 종료 요청
                    jobOperator.stop(executionId)
                    logger.info("배치 JOB 종료 요청")
                } catch (e: Exception) {
                    logger.error("배치 JOB 종료 요청 실패: ${e.message}")
                    try {
                        // stop() 실패 시 abandon()으로 강제 종료 시도
                        jobOperator.abandon(executionId)
                        logger.info("배치 JOB 강제종료 (abandon) 요청")
                    } catch (ex: Exception) {
                        logger.error("배치 JOB 강제종료 (abandon) 실패: ${ex.message}")
                    }
                }
            }
            withContext(Dispatchers.IO) {
                Thread.sleep(10000)
            }
        }
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> $startMessage: ${LocalDateTime.now()}")

        val jobParameters = JobParametersBuilder()
            .addLong("run.id", System.currentTimeMillis())
            .toJobParameters()
        jobLauncher.run(job, jobParameters)
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> $endMessage: ${LocalDateTime.now()}")
    }

    @Scheduled(cron = "0 30 9 ? * MON-FRI", zone = "Asia/Seoul")
    fun runBatchJob() = runBlocking {
        executeJob("newsBatchJob", newsBatchJob, "뉴스 배치 실행 시작", "뉴스 배치 실행 완료")
    }

    @Scheduled(cron = "0 0 0 */5 * *", zone = "Asia/Seoul")
    fun runBatchHolidayJob() = runBlocking {
        executeJob("publicBatchJob", publicBatchJob, "공공API 배치 실행 시작", "공공API 배치 실행 완료")
    }
}