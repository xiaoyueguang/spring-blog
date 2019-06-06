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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

@Transactional
@Rollback
public class AuthLoginControllerTests extends AbstractTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordService passwordService;

    private String username = "testusername";
    private String password = "password";
    private User user;

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
        userRepository.save(user);
    }

    @After
    public void setAfter () {
        userRepository.delete(user);
    }

    @Test
    public void loginView () throws Exception {
        String uri = "/login";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                    .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();

        assertContent(content, "<input type=\"text\" class=\"form-control\" id=\"username\" name=\"username\" placeholder=\"请输入用户名\">");
        assertContent(content, "<input type=\"password\" class=\"form-control\" id=\"password\" name=\"password\" placeholder=\"请输入密码\">");
        assertContent(content, "<a href=\"/register\">没有账号? 注册</a>");
        assertContent(content, "<button type=\"submit\" class=\"btn btn-primary\">登录</button>");
    }

    @Test
    public void loginWithEmptyUsernameAndEmptyPassword () throws Exception {
        String uri = "/login";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .param("username", "")
                        .param("password", "")
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();

        assertContent(content, "<h1>登录失败!</h1>");
        assertContent(content, "<h2>失败原因: <span>找不到用户</span></h2>");
    }

    @Test
    public void loginWithErrorPassword () throws Exception {
        String uri = "/login";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .param("username", username)
                        .param("password", password + "1")
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();

        assertContent(content, "<h1>登录失败!</h1>");
        assertContent(content, "<h2>失败原因: <span>密码不一样</span></h2>");
    }

    @Test
    public void loginWithUnActiveUser () throws Exception {
        String uri = "/login";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .param("username", username)
                        .param("password", password)
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        assertContent(content, "<h1>登录失败!</h1>");
        assertContent(content, "<h2>失败原因: <span>当前账号还未激活! 请先去邮箱激活账号</span></h2>");
    }

    @Test
    public void loginSuccess () throws Exception {
        // 临时设置为邮箱通过
        user.setStatus(1);
        userRepository.save(user);
        String uri = "/login";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .param("username", username)
                        .param("password", password)
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();
        // 重定向
        Assert.assertEquals(302, mvcResult.getResponse().getStatus());
        user.setStatus(1);
        userRepository.save(user);
    }
}
