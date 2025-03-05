package com.sunggyu.news_scraper_batch.batch.service.impl

import com.sunggyu.news_scraper_batch.batch.domain.response.NewsResult
import com.sunggyu.news_scraper_batch.batch.service.ExcelService
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service

@Service
class ExcelServiceImpl: ExcelService {
    override fun createExcelFile(newsList: List<NewsResult>, startDate: String, endDate: String): ByteArray {
        val workbook = XSSFWorkbook()
        //FIXME: - 날짜 가져와서 엑셀파일에 붙여넣기
        val sheet = workbook.createSheet("news")

        val headerRow = sheet.createRow(8)
        val headerCells = listOf("검색어", "뉴스타입", "검색어", "언론사", "제목", "링크")

        headerCells.forEachIndexed{ index, header ->
            val cell = headerRow.createCell(index + 1)
            cell.setCellValue(header)
            val style = workbook.createCellStyle()
            val font = workbook.createFont().apply {
                fontName = "맑은 고딕"
                fontHeightInPoints = 12
                bold = true
            }
            style.setFont(font)
            style.alignment = org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER
            cell.cellStyle = style
        }

        newsList.forEachIndexed { index, newsResult ->
            val row = sheet.createRow(9 + index)
            row.createCell(1).setCellValue(newsResult.newsType)
            row.createCell(2).setCellValue(newsResult.keyword)
            row.createCell(3).setCellValue(newsResult.source)
            row.createCell(4).setCellValue(newsResult.title)
            row.createCell(5).setCellValue(newsResult.link)
        }

        sheet.setColumnWidth(1, 25 * 256)

        return ByteArrayOutputStream().use { bos ->
            workbook.write(bos)
            workbook.close()
            bos.toByteArray()
        }
    }
}