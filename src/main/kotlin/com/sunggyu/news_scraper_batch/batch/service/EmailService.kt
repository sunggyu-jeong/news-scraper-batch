package com.sunggyu.news_scraper_batch.batch.service

interface EmailService {
    fun sendEmail(to: String, cc: Array<String>?, subject: String, text: String, attachmentFilename: String, attachmentData: ByteArray)
}