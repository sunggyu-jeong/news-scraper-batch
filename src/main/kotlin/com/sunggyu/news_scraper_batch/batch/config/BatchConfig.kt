package com.sunggyu.news_scraper_batch.batch.config

import com.sunggyu.news_scraper_batch.batch.tasklet.*
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
class BatchConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val loginTasklet: LoginTasklet,
    private val keywordTasklet: KeywordTasklet,
    private val newsSearchTasklet: NewsSearchTasklet,
    private val fileTasklet: FileTasklet,
    private val selectHolidayTasklet: SelectHolidayTasklet
) {
   @Bean
   fun loginStep(): Step {
       return StepBuilder("loginStep", jobRepository)
           .allowStartIfComplete(true)
           .tasklet(loginTasklet, transactionManager)
           .build()
   }

    @Bean
    fun keywordStep(): Step {
        return StepBuilder("keywordStep", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(keywordTasklet, transactionManager)
            .build()
    }

    @Bean
    fun newsSearchStep(): Step {
        return StepBuilder("newsSearchStep", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(newsSearchTasklet, transactionManager)
            .build()
    }

    @Bean
    fun fileStep(): Step {
        return StepBuilder("fileStep", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(fileTasklet, transactionManager)
            .build()
    }

    @Bean
    fun selectHolidayStep(): Step {
        return StepBuilder("selectHolidayStep", jobRepository)
            .allowStartIfComplete(true)
            .tasklet(selectHolidayTasklet, transactionManager)
            .build()
    }

    @Bean
    fun newsBatchJob(): Job {
        return JobBuilder("newsBatchJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(selectHolidayStep())
            .next(loginStep())
            .next(keywordStep())
            .next(newsSearchStep())
            .next(fileStep())
            .build()
    }
}