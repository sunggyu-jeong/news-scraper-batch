package com.sunggyu.news_scraper_batch.batch.interceptor

import okhttp3.logging.HttpLoggingInterceptor

object LoggingInterceptorProvider {
    val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}