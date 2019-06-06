package com.ray.blog.service;

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

    @Test
    public void sendMailTest () throws Exception {
        mailService.sendSimpleMail("linjilei@qq.com", "这是标题", "这是内容");
    }
}
