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

import javax.transaction.Transactional;

@Transactional
@Rollback
public class HomeControllerTests extends AbstractTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordService passwordService;

    private String username = "testusername";
    private String nickname = "testnickname";
    private String password = "password";
    private User user;

    @Override
    @Before
    public void setBefore () {
        super.setBefore();
        user = new User(
                username,
                nickname,
                passwordService.sha1Password(password, username),
                "test@test.com"
        );
        user.setStatus(1);
        user.setUid();
        userRepository.save(user);
    }

    @After
    public void setAfter () {
        userRepository.delete(user);
    }
    @Test
    public void IndexTestWithLgout() throws Exception {
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

    @Test
    public void IndexTestWithLogin () throws Exception {
        String uri = "/";
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("uid", user.getUid());

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .session(session)
                        .accept(MediaType.TEXT_HTML_VALUE)

        ).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertContent(content, "<a class=\"btn btn-link\" href=\"/user\">用户中心</a>");
    }
}
