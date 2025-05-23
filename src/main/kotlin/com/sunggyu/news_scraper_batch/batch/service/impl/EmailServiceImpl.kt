package com.sunggyu.news_scraper_batch.batch.service.impl

import com.sunggyu.news_scraper_batch.batch.service.EmailService
import org.springframework.core.io.ByteArrayResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(private val mailSender: JavaMailSender): EmailService {
    override fun sendEmail(to: Array<String>?, cc: Array<String>?, subject: String, text: String, attachmentFilename: String, attachmentData: ByteArray) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        to?.let {
            helper.setTo(it)
        }
        cc?.let {
            helper.setCc(cc)
        }
        helper.setSubject(subject)
        helper.setText(text)
        helper.addAttachment(attachmentFilename, ByteArrayResource(attachmentData))

        mailSender.send(message)
    }
}
