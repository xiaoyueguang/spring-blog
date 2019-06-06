package com.ray.blog.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class HomeControllerTests extends AbstractTest {

    @Override
    @Before
    public void setBefore () {
        super.setBefore();
    }

    @Test
    public void IndexTest() throws Exception {
        String uri = "/";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.TEXT_HTML_VALUE)

        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        // 验证标题
        assertContent(content, "<title>首页</title>");
        // 验证内容
        assertContent(content, "<a class=\"navbar-brand\" href=\"/\">博客系统</a>");
        // 验证登录状态
        assertContent(content, "<a class=\"btn btn-link\" href=\"/login\">登录</a>");
        // 验证内容和页码
        assertContent(content, "<li class=\"page-item disabled\"><a class=\"page-link\" href=\"/\">首页</a></li>");
    }
}
