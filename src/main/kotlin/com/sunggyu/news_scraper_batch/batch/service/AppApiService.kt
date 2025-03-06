package com.sunggyu.news_scraper_batch.batch.service

import com.sunggyu.news_scraper_batch.batch.domain.common.ApiResponse
import com.sunggyu.news_scraper_batch.batch.domain.request.LoginRequest
import com.sunggyu.news_scraper_batch.batch.domain.response.KeywordResponse
import com.sunggyu.news_scraper_batch.batch.domain.response.LoginResponse
import com.sunggyu.news_scraper_batch.batch.domain.response.NewsResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import retrofit2.Call

@Service
class AppApiService(
    private val apiService: ApiService,
    @Value("\${username}") private val username: String,
    @Value("\${password}") private val password: String
) {
    private val logger: Logger = LoggerFactory.getLogger(ApiService::class.java)

    fun getNews(queries: String,startDate: String, endDate: String): Call<ApiResponse<List<NewsResponse>>> {
        try {
            return apiService.getNewsList(queries, startDate, endDate)
        } catch (e: Exception) {
            logger.error("뉴스 데이터 조회 중 오류발생: {}", e.message, e)
            throw Exception("뉴스 데이터 조회 중 오류발생: ${e.message}")
        }
    }

    fun requestLogin(): Call<ApiResponse<LoginResponse>> {
        try {
            val request = LoginRequest(username, password)
            return apiService.requestLogin(request)
        } catch (e: Exception) {
            logger.error("로그인 요청 중 오류발생: {}", e.message, e)
            throw Exception(e.message)
        }
    }

    fun getKeywordList(): Call<ApiResponse<List<KeywordResponse>>> {
        try {
            return apiService.getKeywordList()
        } catch (e: Exception) {
            logger.error("검색어 목록 조회 중 오류발생: {}", e.message, e)
            throw Exception(e.message)
        }
    }
}