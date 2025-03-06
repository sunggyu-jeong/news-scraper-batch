package com.sunggyu.news_scraper_batch.batch.config

import com.sunggyu.news_scraper_batch.batch.interceptor.AuthInterceptor
import com.sunggyu.news_scraper_batch.batch.service.ApiService
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Configuration
class RetrofitConfig(
    @Value("\${news_scraper_batch_base_url}") private val baseUrl: String,
    private val authInterceptor: AuthInterceptor
) {
    @Bean
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(10, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .build()
    }

    @Bean
    fun retrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Bean
    fun apiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}