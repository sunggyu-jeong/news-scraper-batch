package com.sunggyu.news_scraper_batch.batch.service

import com.sunggyu.news_scraper_batch.batch.domain.response.NewsResponse

interface ExcelService {
    fun createExcelFile(newsList: List<NewsResponse>, startDate: String, endDate: String): ByteArray
}