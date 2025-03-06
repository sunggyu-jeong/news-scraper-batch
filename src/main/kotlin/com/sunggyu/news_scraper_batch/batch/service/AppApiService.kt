package com.sunggyu.news_scraper_batch.batch.service

import com.sunggyu.news_scraper_batch.batch.domain.common.ApiResponse
import com.sunggyu.news_scraper_batch.batch.domain.request.LoginRequest
import com.sunggyu.news_scraper_batch.batch.domain.response.KeywordResponse
import com.sunggyu.news_scraper_batch.batch.domain.response.LoginResponse
import com.sunggyu.news_scraper_batch.batch.domain.response.NewsResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AppApiService(
    private val apiService: ApiService,
    @Value("\${login.username}") private val username: String,
    @Value("\${login.password}") private val password: String
) {
    suspend fun getNews(queries: String,startDate: String, endDate: String): ApiResponse<NewsResponse> {
        try {
            return apiService.getNewsList(queries, startDate, endDate)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e.message)
        }
    }

    suspend fun requestLogin(): ApiResponse<LoginResponse>? {
        try {
            val request = LoginRequest(username, password)
            return apiService.requestLogin(request)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e.message)
        }
    }
}