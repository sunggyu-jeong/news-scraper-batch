package com.sunggyu.news_scraper_batch.batch.config

import com.sunggyu.news_scraper_batch.batch.tasklet.FetchHolidayTasklet
import com.sunggyu.news_scraper_batch.batch.tasklet.TruncateHolidayTasklet
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class PublicBatchConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val truncateHolidayTasklet: TruncateHolidayTasklet,
    private val fetchHolidayTasklet: FetchHolidayTasklet
) {
    @Bean
    fun truncateHolidayStep(): Step {
        return StepBuilder("truncateHolidayStep", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(truncateHolidayTasklet, transactionManager)
            .build()
    }

    @Bean
    fun fetchHolidayStep(): Step {
        return StepBuilder("fetchHolidayStep", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(fetchHolidayTasklet, transactionManager)
            .build()
    }

    @Bean
    fun publicBatchJob(): Job {
        return JobBuilder("publicBatchJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(truncateHolidayStep())
            .next(fetchHolidayStep())
            .build()
    }
}
