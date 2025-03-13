package com.sunggyu.news_scraper_batch.batch.tasklet

import com.sunggyu.news_scraper_batch.batch.runWithRetry
import com.sunggyu.news_scraper_batch.batch.service.impl.ApiServiceImpl
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component

@Component
@StepScope
class KeywordTasklet(
    private val apiServiceImpl: ApiServiceImpl
): Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        runWithRetry(contribution.stepExecution) {
            apiServiceImpl
                .getKeywordList()
                .execute()
                .body()
                ?.data
                ?.also { keywords ->
                    println(">>>>>>>>>>>>>>>>>>>>>>>>>> 검색어 조회 성공 ${keywords}")
                    val jobExecutionContext = chunkContext.stepContext.stepExecution.jobExecution.executionContext
                    jobExecutionContext.putString("queries", keywords.joinToString(",") { it.keyword })
                } ?: throw RuntimeException(">>>>>>>>>>>>>>>>>>>>>>>>>> API 요청이 실패 했습니다.")
        }
        return RepeatStatus.FINISHED
    }
}