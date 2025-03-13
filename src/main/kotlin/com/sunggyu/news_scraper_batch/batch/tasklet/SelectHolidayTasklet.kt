package com.sunggyu.news_scraper_batch.batch.tasklet

import com.sunggyu.news_scraper_batch.batch.convertToLocalDate
import com.sunggyu.news_scraper_batch.batch.mapper.HolidayMapper
import com.sunggyu.news_scraper_batch.batch.runWithRetry
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import kotlin.math.abs

@Component
@StepScope
class SelectHolidayTasklet(
    private val  holidayMapper: HolidayMapper
): Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        val jobExecutionContext = chunkContext.stepContext.stepExecution.jobExecution.executionContext
        var locDateList: Set<LocalDate> = emptySet()
        runWithRetry(contribution.stepExecution) {
            locDateList = holidayMapper.selectHolidays().map { convertToLocalDate(it.locdate) }.toSet()
        }

        val today: LocalDate = convertToLocalDate(getPastDate())

        // 오늘이 공휴일일 경우, 배치 실행하지 않음.
        if (locDateList.contains(today)) {
            contribution.stepExecution.setTerminateOnly()
            return RepeatStatus.FINISHED
        }

        val previousBusinessDay = getPreviousBusinessDay(today,locDateList)
        jobExecutionContext.put("consecutiveHolidays", previousBusinessDay)
        return RepeatStatus.FINISHED
    }

    fun getPreviousBusinessDay(date: LocalDate, holidays: Set<LocalDate>): LocalDate {
        var previous = date.minusDays(1)
        // 주말이거나, 공휴일일 경우 조회하지 않는다.
        while (previous.dayOfWeek == DayOfWeek.SATURDAY ||
            previous.dayOfWeek == DayOfWeek.SUNDAY ||
            holidays.contains(previous)) {
            previous = previous.minusDays(1)
        }
        return previous
    }

    private fun getPastDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
}