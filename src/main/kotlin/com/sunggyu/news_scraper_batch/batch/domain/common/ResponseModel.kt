package com.sunggyu.news_scraper_batch.batch.domain.common

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val messageDev: String,
    val data: T
)