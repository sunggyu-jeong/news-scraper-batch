package com.sunggyu.news_scraper_batch.batch.service.impl

import com.sunggyu.news_scraper_batch.batch.domain.response.PublicApiResponse
import com.sunggyu.news_scraper_batch.batch.service.PublicApiService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import retrofit2.Call

@Service
class PublicApiServiceImpl (
    private val publicApiService: PublicApiService,
    @Value("\${serviceKey}") private val serviceKey: String,
) {
    private val logger: Logger = LoggerFactory.getLogger(publicApiService::class.java)

    fun getHolidayList(
        solYear: String
    ): Call<PublicApiResponse> {
        try {
            return publicApiService.getHolidayList(serviceKey, solYear)
        } catch (e: Exception) {
            logger.error(">>>>>>>>>>>>>>>>>> 공공 API 공휴일 조회 중 오류발생 ${e.message}")
            throw Exception("공공 API 공휴일 조회 중 오류발생: ${e.message}")
        }
    }
}