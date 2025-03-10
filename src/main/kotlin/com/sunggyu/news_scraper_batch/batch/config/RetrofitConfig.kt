package com.sunggyu.news_scraper_batch.batch.config

import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.sunggyu.news_scraper_batch.batch.interceptor.AuthInterceptor
import com.sunggyu.news_scraper_batch.batch.interceptor.LoggingInterceptorProvider
import com.sunggyu.news_scraper_batch.batch.service.ApiService
import com.sunggyu.news_scraper_batch.batch.service.PublicApiService
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Configuration
class RetrofitConfig(
    @Value("\${news_scraper_batch_base_url}") private val baseUrl: String,
    @Value("\${news_scraper_batch_public_url}") private val publicUrl: String,
    private val authInterceptor: AuthInterceptor
) {
    val gson = GsonBuilder()
        .setStrictness(Strictness.LENIENT)
        .create()

    @Bean(name = ["okHttpClient"])
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(LoggingInterceptorProvider.loggingInterceptor)
            .connectTimeout(10, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .build()
    }

    @Bean(name = ["okHttpClientForPublic"])
    fun okHttpClientForPublic(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptorProvider.loggingInterceptor)
            .connectTimeout(10, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .build()
    }

    @Bean
    fun retrofit(@Qualifier("okHttpClient") client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Bean
    fun retrofitForPublic(@Qualifier("okHttpClientForPublic") client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(publicUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Bean
    fun apiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Bean
    fun publicApiService(retrofitForPublic: Retrofit): PublicApiService = retrofitForPublic.create(PublicApiService::class.java)
}