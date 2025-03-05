package com.sunggyu.news_scraper_batch.batch.config

import com.sunggyu.news_scraper_batch.batch.service.ApiService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Configuration
class RetrofitConfig(@Value("\${news_scraper_batch_base_url}") private val baseUrl: String) {
    @Bean
    fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Bean
    fun apiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}