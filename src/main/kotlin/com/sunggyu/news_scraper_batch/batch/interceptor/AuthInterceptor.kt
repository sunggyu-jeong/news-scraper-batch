package com.sunggyu.news_scraper_batch.batch.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AuthInterceptor: Interceptor {
    @Volatile
    private var token: String? = null

    private val logger: Logger = LoggerFactory.getLogger(AuthInterceptor::class.java)

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        val response = chain.proceed(requestBuilder.build())

        val responseBodyString = response.body()?.string() ?: ""

        try {
            val json = JSONObject(responseBodyString)
            val jsonObj = (json.get("data") as? JSONObject ?: JSONObject());
            if (jsonObj.has("accessToken")) {
                token = jsonObj.getString("accessToken")
            }
        } catch (e: Exception) {
            logger.error("엑세스토큰 설정 중 오류발생: {}", e.message, e)
        }

        val mediaType = response.body()?.contentType()
        val newResponseBody = ResponseBody.create(mediaType, responseBodyString)

        return response.newBuilder().body(newResponseBody).build()
    }
}