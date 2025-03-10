package com.sunggyu.news_scraper_batch.batch.tasklet

import com.sunggyu.news_scraper_batch.batch.domain.response.toDomain
import com.sunggyu.news_scraper_batch.batch.mapper.HolidayMapper
import com.sunggyu.news_scraper_batch.batch.runWithRetry
import com.sunggyu.news_scraper_batch.batch.service.impl.PublicApiServiceImpl
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
@StepScope
class FetchHolidayTasklet(
    private val publicApiServiceImpl: PublicApiServiceImpl,
    private val holidayMapper: HolidayMapper
): Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        runWithRetry(contribution.stepExecution) {
            val currentYear = LocalDate.now().year.toString()
            val response = publicApiServiceImpl
                .getHolidayList(currentYear)
                .execute()
            response.body()
                    ?.toDomain()
                    ?.takeIf { it.isNotEmpty() }
                    ?.let { items ->
                        holidayMapper.addHolidays(items)
                    }
        }

        return RepeatStatus.FINISHED
    }
}