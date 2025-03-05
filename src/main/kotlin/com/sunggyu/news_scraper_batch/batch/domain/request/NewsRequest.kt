package com.sunggyu.news_scraper_batch.batch.domain.request

data class NewsRequest(val queries: String, val startDate: String, val endDate: String)