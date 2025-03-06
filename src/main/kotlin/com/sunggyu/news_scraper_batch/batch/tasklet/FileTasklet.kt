package com.sunggyu.news_scraper_batch.batch.tasklet

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sunggyu.news_scraper_batch.batch.domain.response.NewsResponse
import com.sunggyu.news_scraper_batch.batch.getPastDate
import com.sunggyu.news_scraper_batch.batch.service.EmailService
import com.sunggyu.news_scraper_batch.batch.service.ExcelService
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component

@Component
@StepScope
class FileTasklet(
    private val excelService: ExcelService,
    private val emailService: EmailService
): Tasklet {
    val gson = Gson()

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        val jobExecutionContext = chunkContext.stepContext.stepExecution.jobExecution.executionContext

        try {
            val newsChunk = jobExecutionContext.get("newsList") as String
            val type = object : TypeToken<List<NewsResponse>>() {}.type
            val restoredList: List<NewsResponse> = gson.fromJson(newsChunk, type)

            if (restoredList.isEmpty()) throw Error("조회된 정보가 없습니다.")
            val startDate = getPastDate()
            val endDate = getPastDate(0)

            val excelBytes = excelService.createExcelFile(restoredList, startDate, endDate)

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
                    |""".trimMargin(),
                attachmentFilename = "${endDate} news.xlsx",
                attachmentData = excelBytes
            )
        } catch(e: Exception) {
            println("엑셀 데이터 생성 또는 이메일 전송 실패 $e")
            throw RuntimeException("엑셀 데이터 생성 또는 이메일 전송 실패 $e")
        }
        return RepeatStatus.FINISHED
    }
}