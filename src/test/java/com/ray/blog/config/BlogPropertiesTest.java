package com.ray.blog.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BlogPropertiesTest {
    @Autowired
    private BlogProperties blogProperties;

    @Test
    public void test() throws Exception {
        Assert.assertEquals(blogProperties.getTitle(), "博客系统");
    }
}
