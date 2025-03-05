package com.sunggyu.news_scraper_batch.batch.service

import com.sunggyu.news_scraper_batch.batch.domain.response.NewsResult

interface ExcelService {
    fun createExcelFile(newsList: List<NewsResult>, startDate: String, endDate: String): ByteArray
}