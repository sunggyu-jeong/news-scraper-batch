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
class LoginTasklet(
    private val appApiService: AppApiService
): Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        runWithRetry(contribution.stepExecution) {
            appApiService
                .requestLogin()
                .execute()
        }
        return RepeatStatus.FINISHED
    }
}