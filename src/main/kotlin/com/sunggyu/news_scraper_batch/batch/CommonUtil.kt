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
    var attempt = 0
    while (attempt < maxRetries) {
        attempt++
        try {
            return block()
        } catch(e: Exception) {
            println("요청 실패 (시도 횟수: $attempt) (오류 메세지: ${e.message}")
            if (attempt == maxRetries) {
                stepExecution.status = BatchStatus.FAILED
                stepExecution.exitStatus = org.springframework.batch.core.ExitStatus.FAILED
                return null
            }
            Thread.sleep(delayMillis)
        }
    }
    return null
}

fun getPastDate(daysAgo: Long = 1): String {
    return LocalDateTime.now().minusDays(daysAgo).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
}

fun convertToLocalDate(date: Date): LocalDate {
    return date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}