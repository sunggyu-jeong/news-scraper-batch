package com.sunggyu.news_scraper_batch.batch.scheduler

import com.sunggyu.news_scraper_batch.batch.service.AppApiService
import com.sunggyu.news_scraper_batch.batch.service.EmailService
import com.sunggyu.news_scraper_batch.batch.service.ExcelService
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class BatchScheduler(
    private val appApiService: AppApiService,
    private val excelService: ExcelService,
    private val emailService: EmailService
) {
    private val logger: Logger = LoggerFactory.getLogger(BatchScheduler::class.java)

    @Scheduled(cron = "0 */5 * * * ?")
    fun runBatchJob() = runBlocking {
        execute()
    }

//    @Scheduled(cron = "0 0 9 ? * MON-FRI")
    suspend fun execute() {
        try {
            // 1. 로그인 요청으로 엑세스 토큰 설정
            val loginResponse = appApiService.requestLogin();
            logger.debug(">>>>>>>>>>>>>> 로그인 응답값 {}", loginResponse)
            // 2. 사용자의 키워도 믁록 조회
            val keywordResponse = appApiService.getKeywordList()
            logger.debug(">>>>>>>>>>>>>> 검색어 응답값 {}", keywordResponse)
            val queries = keywordResponse.data.map { el -> el.keyword }.joinToString(",")
            // 3. 검색어, 검색일을 기준으로 뉴스정보 조회
            // 검색일: 조회일 기준 어제날짜 ~ 당일
            val startDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            val endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            val newsList = appApiService.getNews(queries, startDate, endDate)
            logger.debug(">>>>>>>>>>>>>> 뉴스 결과값 {}", newsList)

            // 4. 검색된 정보로 엑셀파일 생성
            val excelBytes = excelService.createExcelFile(newsList.data, startDate, endDate)

            emailService.sendEmail(
                to = "coder3306@goodsoft.io",
                cc = null,
                subject = "[${endDate}] 최신 뉴스 공유드립니다.",
                text = """
                    |안녕하세요,
                    |${endDate}의 최신 뉴스를 정리하여 첨부해드립니다.
                    |유용한 정보가 되길 바라며, 궁금한 사항이 있으시면 언제든지 문의 주세요.
                    |
                    |항상 건강하고 좋은 하루 보내세요!
                    |
                    |감사합니다.
                """.trimMargin(),
                attachmentFilename = "${endDate} news.xlsx",
                attachmentData = excelBytes
            )
        } catch (e: Exception) {
            logger.error(">>>>>>>>>>>>>> 배치 실행 중 {}", e.message)
            throw Error(e);
        }
    }
}