package com.sunggyu.news_scraper_batch.batch.service

import com.sunggyu.news_scraper_batch.batch.domain.response.NewsResponse

interface NewsService {
    fun fetchNewsResults() : List<NewsResponse>
}