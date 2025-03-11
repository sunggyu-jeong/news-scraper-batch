package com.sunggyu.news_scraper_batch.batch.tasklet

import com.google.gson.Gson
import com.sunggyu.news_scraper_batch.batch.getPastDate
import com.sunggyu.news_scraper_batch.batch.runWithRetry
import com.sunggyu.news_scraper_batch.batch.service.impl.ApiServiceImpl
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
@StepScope
class NewsSearchTasklet(
    private val apiServiceImpl: ApiServiceImpl
): Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        val jobExecutionContext = chunkContext.stepContext.stepExecution.jobExecution.executionContext

        runWithRetry(contribution.stepExecution) {
            val queries = jobExecutionContext.get("queries") as String
            val consecutiveHolidays = jobExecutionContext.get("consecutiveHolidays") as LocalDate
            val endDate = getPastDate(0)
            val newsList = apiServiceImpl
                            .getNews(queries,
                                     consecutiveHolidays.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                                     endDate)
                            .execute()
                            .body()
            val jsonString = Gson().toJson(newsList?.data)
            jobExecutionContext.put("newsList", jsonString)
        }
        return RepeatStatus.FINISHED
    }
}