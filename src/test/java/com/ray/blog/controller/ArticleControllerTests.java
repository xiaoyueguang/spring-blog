package com.ray.blog.controller;


import com.ray.blog.model.User;
import com.ray.blog.repository.UserRepository;
import com.ray.blog.service.PasswordService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;

@Transactional
@Rollback
public class ArticleControllerTests extends AbstractTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordService passwordService;

    private String username = "testusername";
    private String nickname = "testnickname";
    private String password = "password";
    private User user;
    MockHttpSession session = new MockHttpSession();

    @Override
    @Before
    public void setBefore () {
        super.setBefore();
        user = new User(
                username,
                "testnickname",
                passwordService.sha1Password(password, username),
                "test@test.com"
        );
        user.setStatus(1);
        user.setUid();
        userRepository.save(user);
        session.setAttribute("uid", user.getUid());
    }

    @After
    public void setAfter () {
        userRepository.delete(user);
    }

    @Test
    public void createView () throws Exception {

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get("/article/create")
                        .session(session)
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());

        String content = mvcResult.getResponse().getContentAsString();

        assertContent(content, "新建文章");
        assertContent(content, "<form action=\"/article/create\" method=\"post\">");
        assertContent(content, "<input type=\"text\" class=\"form-control\" id=\"title\" placeholder=\"请输入文章标题\" name=\"title\" value=\"\" />");
        assertContent(content, "<textarea class=\"form-control\" id=\"content\"  placeholder=\"请输入文章内容\" name=\"content\"></textarea>");
    }

    // ... 以下添加 新增 修改 删除等方法
}
