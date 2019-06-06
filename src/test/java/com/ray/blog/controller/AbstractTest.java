package com.ray.blog.controller;

import com.ray.blog.BlogApplication;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApplication.class)
@WebAppConfiguration
public abstract class AbstractTest {
    protected MockMvc mvc;
    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected void setBefore () {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected void assertContent (String content, String html) throws Exception {
        try {
            Assert.assertTrue(content.indexOf(html) > -1);
        } catch (Exception e) {
            System.out.println(content);
            throw e;
        }
    }
}
