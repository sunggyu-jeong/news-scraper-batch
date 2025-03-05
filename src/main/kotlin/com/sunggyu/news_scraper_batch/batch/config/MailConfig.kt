package com.sunggyu.news_scraper_batch.batch.config

import org.springframework.beans.factory.annotation.Configurable
import org.springframework.context.annotation.Bean
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.Properties

@Configurable
class MailConfig {
    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        //FIXME: - host 확인해서 수정
        mailSender.host = "smtp.gmail.com"
        mailSender.port = 587
        mailSender.username = "sunggyu"
        mailSender.password = "123456"

        val props: Properties = mailSender.javaMailProperties
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = true
        return mailSender
    }
}