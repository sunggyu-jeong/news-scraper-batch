package com.sunggyu.news_scraper_batch.batch.service.impl

import com.sunggyu.news_scraper_batch.batch.domain.response.NewsResponse
import com.sunggyu.news_scraper_batch.batch.service.ExcelService
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFFont
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service

@Service
class ExcelServiceImpl: ExcelService {
    override fun createExcelFile(
        newsList: List<NewsResponse>,
        startDate: String,
        endDate: String
    ): ByteArray {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("$endDate, news")

        // -- 1. 기본 스타일: 전체 셀의 기본 글꼴 설정 ("맑은 고딕", 12pt, 일반, 검정) --
        val defaultFont = workbook.createFont().apply {
            fontName = "맑은 고딕"
            fontHeightInPoints = 12
            bold = false
            color = IndexedColors.BLACK.index
        }
        val defaultCellStyle = workbook.createCellStyle().apply {
            setFont(defaultFont)
        }

        // -- 2. 헤더 스타일 설정 (헤더 셀: B9, F9, G9, H9, I9, J9 → 인덱스: 1,5,6,7,8,9) --
        val headerRowIndex = 8 // Excel의 9번째 행
        val headerRow = sheet.createRow(headerRowIndex)
        headerRow.heightInPoints = 30f

        val headerTexts = listOf("검색어", "뉴스타입", "검색어", "언론사", "타이틀", "링크")
        val headerColIndices = listOf(1, 5, 6, 7, 8, 9)

        val headerFont = workbook.createFont().apply {
            fontName = "맑은 고딕"
            fontHeightInPoints = 12
            bold = true
        }
        // 폰트 색상: 0A3D62 (RGB: 10, 61, 98)
        if (headerFont is XSSFFont) {
            headerFont.setColor(XSSFColor(java.awt.Color(10, 61, 98), null))
        }
        val headerStyle = workbook.createCellStyle().apply {
            setFont(headerFont)
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            fillPattern = FillPatternType.SOLID_FOREGROUND
            if (this is XSSFCellStyle) {
                setFillForegroundColor(XSSFColor(java.awt.Color(214, 230, 245), null)) // D6E6F5
            }
        }
        headerColIndices.forEachIndexed { index, colIndex ->
            val cell = headerRow.createCell(colIndex)
            cell.setCellValue(headerTexts[index])
            cell.cellStyle = headerStyle
        }

        // -- 3. F1, F2 셀에 볼드 스타일 적용 (F열: 인덱스 5) --
        val boldFont = workbook.createFont().apply {
            fontName = "맑은 고딕"
            fontHeightInPoints = 12
            bold = true
        }
        if (boldFont is XSSFFont) {
            boldFont.setColor(XSSFColor(java.awt.Color(10, 61, 98), null))
        }
        val boldStyle = workbook.createCellStyle().apply {
            setFont(boldFont)
            fillPattern = FillPatternType.SOLID_FOREGROUND
            if (this is XSSFCellStyle) {
                setFillForegroundColor(XSSFColor(java.awt.Color(214, 230, 245), null))
            }
        }
        for (r in 0..1) {
            val row = sheet.getRow(r) ?: sheet.createRow(r)
            val cell = row.createCell(5)
            cell.cellStyle = boldStyle
        }

        // -- 4. G1, G2 셀 가운데 정렬 (G열: 인덱스 6) --
        val centerStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
        }
        for (r in 0..1) {
            val row = sheet.getRow(r) ?: sheet.createRow(r)
            val cell = row.getCell(6) ?: row.createCell(6)
            cell.cellStyle = centerStyle
        }

        // -- 5. 열 너비 설정 (0~9열, 단위: 문자 폭 * 256) --
        val colWidths = listOf(10, 25, 10, 10, 10, 15, 20, 30, 80, 150)
        colWidths.forEachIndexed { index, width ->
            sheet.setColumnWidth(index, width * 256)
        }

        // -- 6. 데이터 행 생성 및 채우기 --
        // Vue에서는 data row를 10열짜리 배열로 구성:
        // [empty, uniqueKeyword, empty, empty, empty, newsType, keyword, source, title, link]
        val uniqueKeywords = newsList.map { it.keyword }.distinct()
        newsList.forEachIndexed { index, newsResult ->
            val row = sheet.createRow(9 + index)
            row.createCell(0).setCellValue("")
            val keywordValue = if (index < uniqueKeywords.size) uniqueKeywords[index] else ""
            row.createCell(1).setCellValue(keywordValue)
            row.createCell(2).setCellValue("")
            row.createCell(3).setCellValue("")
            row.createCell(4).setCellValue("")
            row.createCell(5).setCellValue(newsResult.newsType)
            row.createCell(6).setCellValue(newsResult.keyword)
            row.createCell(7).setCellValue(newsResult.source)
            // 타이틀 셀 (I열, 인덱스 8)
            val titleCell = row.createCell(8)
            titleCell.setCellValue(newsResult.title)
            titleCell.cellStyle = defaultCellStyle
            // 링크 셀 (J열, 인덱스 9)
            row.createCell(9).setCellValue(newsResult.link)
        }

        // -- 7. 사용자 정의 스타일 적용 --
        // 각 데이터 행에 대해, 타이틀 셀(열 I, 인덱스 8)에서 뉴스 제목이 키워드 셀(열 G, 인덱스 6)의 값 포함시 폰트를 빨간색(FF0000)으로 변경
        for (i in newsList.indices) {
            val row = sheet.getRow(9 + i) ?: continue
            val keywordCell = row.getCell(6)
            val titleCell = row.getCell(8)
            if (keywordCell != null && titleCell != null) {
                val keywordText = keywordCell.stringCellValue
                if (titleCell.stringCellValue.contains(keywordText)) {
                    val redFont = workbook.createFont().apply {
                        fontName = "맑은 고딕"
                        fontHeightInPoints = 12
                        color = IndexedColors.RED.index
                    }
                    val redStyle = workbook.createCellStyle().apply {
                        setFont(redFont)
                    }
                    titleCell.cellStyle = redStyle
                }
            }
        }

        // -- 8. 워크북을 바이트 배열로 반환 --
        return ByteArrayOutputStream().use { bos ->
            workbook.write(bos)
            workbook.close()
            bos.toByteArray()
        }
    }
}