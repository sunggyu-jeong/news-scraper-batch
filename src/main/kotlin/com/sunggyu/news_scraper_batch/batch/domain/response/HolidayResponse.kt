package com.sunggyu.news_scraper_batch.batch.domain.response

import java.text.SimpleDateFormat
import java.util.*

data class PublicApiResponse (
    val response: PublicResponseBody
)

data class PublicResponseBody (
    val body: ResponseDetails
)

data class ResponseDetails (
    val items: HolidayItems
)

data class HolidayItems (
    val item: List<HolidayResponse>
)

data class HolidayResponse (
    val dateName: String,
    val isHoliday: String,
    val locdate: Long,
    val seq: Int
)

data class HolidayDomain (
    val dateName: String,
    val isHoliday: String,
    val locdate: Date,
    val seq: Int
)

fun PublicApiResponse.toDomain(): List<HolidayDomain> {
    val formatter = SimpleDateFormat("yyyyMMdd")
    return this.response.body.items.item.map { el ->
        formatter.timeZone = TimeZone.getTimeZone("Asia/Seoul")
        val date = formatter.parse(el.locdate.toString())
        HolidayDomain(
            dateName = el.dateName,
            isHoliday = el.isHoliday,
            locdate = date,
            seq = el.seq
        )
    }
}