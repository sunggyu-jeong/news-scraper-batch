package com.sunggyu.news_scraper_batch.batch.scheduler

import org.springframework.stereotype.Component

@Component
class BatchScheduler(
    private val newsService: NewsService,

)