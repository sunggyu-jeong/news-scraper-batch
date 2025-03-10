package com.sunggyu.news_scraper_batch.batch.tasklet

import com.sunggyu.news_scraper_batch.batch.mapper.HolidayMapper
import com.sunggyu.news_scraper_batch.batch.runWithRetry
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component

@Component
@StepScope
class TruncateHolidayTasklet(
    private val holidayMapper: HolidayMapper
): Tasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        runWithRetry(contribution.stepExecution) {
            holidayMapper.truncateHolidays()
        }

        return RepeatStatus.FINISHED
    }
}