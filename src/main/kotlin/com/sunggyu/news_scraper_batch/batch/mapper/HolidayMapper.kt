package com.sunggyu.news_scraper_batch.batch.mapper

import com.sunggyu.news_scraper_batch.batch.domain.response.HolidayDomain
import org.apache.ibatis.annotations.Mapper

@Mapper
interface HolidayMapper {
    fun truncateHolidays()
    fun addHolidays(items: List<HolidayDomain>)
}