package com.ray.blog.service;

import com.ray.blog.config.BlogProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTests {
    @Autowired
    private MailService mailService;
    @Autowired
    private BlogProperties blogProperties;
    @Test
    public void sendMailTest () throws Exception {
        mailService.sendSimpleMail(blogProperties.getTestEmail(), "这是标题", "这是内容");
        mailService.sendHtmlMail(blogProperties.getTestEmail(), "这是标题", "<p>这是内容</p>");
    }
}
