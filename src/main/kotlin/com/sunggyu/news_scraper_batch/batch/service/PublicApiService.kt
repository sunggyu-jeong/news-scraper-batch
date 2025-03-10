package com.sunggyu.news_scraper_batch.batch.service

import com.sunggyu.news_scraper_batch.batch.domain.response.PublicApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PublicApiService {
    @GET("openapi/service/SpcdeInfoService/getRestDeInfo")
    fun getHolidayList(@Query("serviceKey", encoded = true) serviceKey: String,
                       @Query("solYear") solYear: String,
                       @Query("numOfRows") numOfRows: Int = 50,
                       @Query("_type") tlype: String = "json"): Call<PublicApiResponse>
}