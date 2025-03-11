package com.sunggyu.news_scraper_batch.batch.domain.response

data class AutomailResponse (
    val id: Int,
    val name: String,
    val email: String,
    val recipientType: String
)