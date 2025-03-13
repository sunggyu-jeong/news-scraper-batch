package com.sunggyu.news_scraper_batch.batch

import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.StepExecution
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

fun <T> runWithRetry(
    stepExecution: StepExecution,
    maxRetries: Int = 3,
    delayMillis: Long = 5000L,
    block: () -> T): T? {
    repeat(maxRetries - 1) { attempt ->
        try {
            return block()
        } catch (e: Exception) {
            println("요청 실패 (시도 횟수: $attempt)")
            Thread.sleep(delayMillis)
        }
    }
    return try {
        block()
    } catch(e: Exception) {
        println("마지막 시도 실패 (시도 횟수: $maxRetries) (오류 메세지: ${e.message})")
        stepExecution.status = BatchStatus.FAILED
        stepExecution.addFailureException(e)

        throw e
    }
}

fun getPastDate(daysAgo: Long = 1): String {
    return LocalDateTime.now().minusDays(daysAgo).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
}

fun convertToLocalDate(date: Date): LocalDate {
    return date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}