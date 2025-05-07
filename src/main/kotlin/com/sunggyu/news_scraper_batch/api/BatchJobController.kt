package com.sunggyu.news_scraper_batch.api

import com.sunggyu.news_scraper_batch.api.domain.request.JobRequest
import com.sunggyu.news_scraper_batch.batch.domain.common.ApiResponse
import com.sunggyu.news_scraper_batch.batch.scheduler.BatchScheduler
import kotlinx.coroutines.runBlocking
import org.springframework.batch.core.Job
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/batchjob")
@CrossOrigin(origins = ["http://localhost:8080", "https://news-scraper.pages.dev"])
class BatchJobController(
    private val batchScheduler: BatchScheduler,
    private val newsBatchJob: Job,
    private val publicBatchJob: Job,
) {
    private enum class BatchJobName(val value: String) {
        NewsBatchJob("newsBatchJob"),
        PublicBatchJob("publicBatchJob")
    }

    @PostMapping("/manual/run")
    fun runManualJob(@RequestBody request: JobRequest): ResponseEntity<ApiResponse<String>> {
        val validJob = BatchJobName
                        .entries
                        .find { it.value == request.jobName } ?: throw IllegalArgumentException("유효하지 않은 접근입니다.: ${request.jobName}")
        runBlocking {
            when (validJob) {
                BatchJobName.NewsBatchJob -> batchScheduler.executeJob("newsBatchJob", newsBatchJob, "뉴스 배치 실행 시작", "뉴스 배치 실행 완료")
                BatchJobName.PublicBatchJob -> batchScheduler.executeJob("publicBatchJob", publicBatchJob, "공공API 배치 실행 시작", "공공API 배치 실행 완료")
            }
        }
        val response = ResponseEntity.ok(ApiResponse(200, "배치 수동실행 요청이 완료되었습니다.", "요청 성공" , request.jobName))
        return response
    }
}