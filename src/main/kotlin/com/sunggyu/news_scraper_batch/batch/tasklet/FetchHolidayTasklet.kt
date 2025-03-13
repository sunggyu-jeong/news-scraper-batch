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
            publicApiServiceImpl
                .getHolidayList(currentYear)
                .execute()
                .body()
                ?.also { holidayResponse ->
                    holidayResponse
                        .toDomain()
                        .takeIf { it.isNotEmpty() }
                        ?.let { holidayMapper.addHolidays(it) }
                } ?: throw RuntimeException("데이터베이스 응답이 실패했습니다.")
        }

        return RepeatStatus.FINISHED
    }
}