package com.sunggyu.news_scraper_batch.batch.tasklet

import com.sunggyu.news_scraper_batch.batch.runWithRetry
import com.sunggyu.news_scraper_batch.batch.service.AppApiService
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component

@Component
@StepScope
class KeywordTasklet(
    private val appApiService: AppApiService
): Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        runWithRetry(contribution.stepExecution) {
            val keywordResponse = appApiService
                                    .getKeywordList()
                                    .execute()
            println(">>>>>>>>>>>>>>>>>>>>>>>>>> 검색어 조회 성공 $keywordResponse")
            keywordResponse.body()?.data?.let { keyword ->
                val jobExecutionContext = chunkContext.stepContext.stepExecution.jobExecution.executionContext
                jobExecutionContext.putString("queries", keyword.joinToString(",") { it.keyword })
            }
        }
        return RepeatStatus.FINISHED
    }
}