package com.sunggyu.news_scraper_batch.batch.domain.response

data class NewsResponse (
    val newsType: String,
    val keyword: String,
    val source: String,
    val title: String,
    val link: String,
    val description: String,
    val date: String
)