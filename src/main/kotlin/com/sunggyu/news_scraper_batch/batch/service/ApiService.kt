package com.sunggyu.news_scraper_batch.batch.service

import com.sunggyu.news_scraper_batch.batch.domain.common.ApiResponse
import com.sunggyu.news_scraper_batch.batch.domain.request.LoginRequest
import com.sunggyu.news_scraper_batch.batch.domain.response.KeywordResponse
import com.sunggyu.news_scraper_batch.batch.domain.response.LoginResponse
import com.sunggyu.news_scraper_batch.batch.domain.response.NewsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("api/user/login")
    fun requestLogin(@Body request: LoginRequest): Call<ApiResponse<LoginResponse>>

    @GET("api/keywords")
    fun getKeywordList(): Call<ApiResponse<List<KeywordResponse>>>

    @GET("api/news")
    fun getNewsList(@Query("queries") queries: String, @Query("startDate") startDate: String, @Query("endDate") endDate: String): Call<ApiResponse<List<NewsResponse>>>
}