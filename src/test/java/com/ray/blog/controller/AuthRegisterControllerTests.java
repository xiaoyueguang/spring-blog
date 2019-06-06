package com.ray.blog.controller;

import com.ray.blog.config.BlogProperties;
import com.ray.blog.model.User;
import com.ray.blog.repository.UserRepository;
import com.ray.blog.service.PasswordService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

@Transactional
@Rollback
public class AuthRegisterControllerTests extends AbstractTest {
    private String username = "testusername";
    private String password = "testpassword";
    private String email;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private BlogProperties blogProperties;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private User user;

    @Override
    @Before
    public void setBefore () {
        super.setBefore();
        email = blogProperties.getTestEmail();
        user = new User(
                username + "1",
                "testnickname1",
                passwordService.sha1Password(password, username),
                blogProperties.getTestEmail2()
        );
        userRepository.save(user);
    }
    @After
    public void setAfter () {
        userRepository.delete(user);
    }

    @Test
    public void registerView () throws Exception {
        String uri = "/register";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();

        assertContent(content, "<form class=\"form-horizontal\" method=\"post\" action=\"/register\">");
        assertContent(content, "<input type=\"text\" class=\"form-control\" id=\"username\" name=\"username\" placeholder=\"请输入用户名\" value=\"\">");
        assertContent(content, "<input type=\"password\" class=\"form-control\" id=\"password\" name=\"password\" placeholder=\"请输入密码\" value=\"\">");
        assertContent(content, "<input type=\"password\" class=\"form-control\" id=\"password2\" name=\"password2\" placeholder=\"请输入确认密码\" value=\"\">");
        assertContent(content, "<input type=\"text\" class=\"form-control\" id=\"email\" name=\"email\" placeholder=\"请输入邮箱\" value=\"\">");
        assertContent(content, "<button type=\"submit\" class=\"btn btn-primary\">注册</button>");
    }
    @Test
    public void registerWithEmptyUsername () throws Exception {
        String uri = "/register";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .param("username", "")
                        .param("password", "")
                        .param("password2", "")
                        .param("email", "")
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals(200, mvcResult.getResponse().getStatus());

        assertContent(content, "<input type=\"text\" class=\"form-control is-invalid\" id=\"username\" name=\"username\" placeholder=\"请输入用户名\" value=\"\">");
        assertContent(content, "<div class=\"invalid-feedback\">请输入用户名</div>");
    }

    @Test
    public void registerWithEmptyPassword () throws Exception {
        String uri = "/register";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .param("username", username)
                        .param("password", "")
                        .param("password2", "")
                        .param("email", "")
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        assertContent(content, "<input type=\"text\" class=\"form-control\" id=\"username\" name=\"username\" placeholder=\"请输入用户名\" value=\"" + username + "\">");
        assertContent(content, "<input type=\"password\" class=\"form-control is-invalid\" id=\"password\" name=\"password\" placeholder=\"请输入密码\" value=\"\">");
        assertContent(content, "<div class=\"invalid-feedback\">请输入密码</div>");
    }

    @Test
    public void registerWithEmptyPassword2 () throws Exception {
        String uri = "/register";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .param("username", username)
                        .param("password", password)
                        .param("password2", "")
                        .param("email", "")
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        assertContent(content, "<input type=\"text\" class=\"form-control\" id=\"username\" name=\"username\" placeholder=\"请输入用户名\" value=\"" + username + "\">");
        assertContent(content, "<input type=\"password\" class=\"form-control\" id=\"password\" name=\"password\" placeholder=\"请输入密码\" value=\"" + password + "\">");
        assertContent(content, "<input type=\"password\" class=\"form-control is-invalid\" id=\"password2\" name=\"password2\" placeholder=\"请输入确认密码\" value=\"\">");
        assertContent(content, "<div class=\"invalid-feedback\">请输入确认密码</div>");
    }

    @Test
    public void registerWithEmptyEmail () throws Exception {
        String uri = "/register";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .param("username", username)
                        .param("password", password)
                        .param("password2", password)
                        .param("email", "")
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        assertContent(content, "<input type=\"text\" class=\"form-control\" id=\"username\" name=\"username\" placeholder=\"请输入用户名\" value=\"" + username + "\">");
        assertContent(content, "<input type=\"password\" class=\"form-control\" id=\"password\" name=\"password\" placeholder=\"请输入密码\" value=\"" + password + "\">");
        assertContent(content, "<input type=\"password\" class=\"form-control\" id=\"password2\" name=\"password2\" placeholder=\"请输入确认密码\" value=\"" + password + "\">");
        assertContent(content, "<input type=\"text\" class=\"form-control is-invalid\" id=\"email\" name=\"email\" placeholder=\"请输入邮箱\" value=\"\">");
        assertContent(content, "<div class=\"invalid-feedback\">请输入电子邮箱</div>");
    }

    @Test
    public void registerWithPasswordNotEqualPassword () throws Exception {
        String uri = "/register";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .param("username", username)
                        .param("password", password)
                        .param("password2", password + "1")
                        .param("email", email)
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        assertContent(content, "<input type=\"text\" class=\"form-control\" id=\"username\" name=\"username\" placeholder=\"请输入用户名\" value=\"" + username + "\">");
        assertContent(content, "<input type=\"password\" class=\"form-control\" id=\"password\" name=\"password\" placeholder=\"请输入密码\" value=\"" + password + "\">");
        assertContent(content, "<input type=\"password\" class=\"form-control is-invalid\" id=\"password2\" name=\"password2\" placeholder=\"请输入确认密码\" value=\"" + password  + "1\">");
        assertContent(content, "<div class=\"invalid-feedback\">两次密码不一致</div>");
        assertContent(content, "<input type=\"text\" class=\"form-control\" id=\"email\" name=\"email\" placeholder=\"请输入邮箱\" value=\"" + email + "\">");
    }


    @Test
    public void registerWithRepeatUsername () throws Exception {
        String uri = "/register";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .param("username", username + "1")
                        .param("password", password)
                        .param("password2", password)
                        .param("email", email)
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        assertContent(content, "已存在该用户");
        assertContent(content, username + "1");
    }

    @Test
    public void registerWithRepeatEmail () throws Exception {
        String uri = "/register";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .param("username", username)
                        .param("password", password)
                        .param("password2", password)
                        .param("email", blogProperties.getTestEmail2())
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals(200, mvcResult.getResponse().getStatus());

        assertContent(content, "已经在该邮箱");
        assertContent(content, blogProperties.getTestEmail2());
    }

    @Test
    public void registerSuccess () throws Exception {
        String uri = "/register";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .param("username", username)
                        .param("password", password)
                        .param("password2", password)
                        .param("email", email)
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        assertContent(content, "注册完成, 请去您的邮箱中激活账号!");
        User user = userRepository.findByUsername(username);

        Assert.assertEquals(user.getNickname(), username);
        Assert.assertEquals(user.getPassword(), passwordService.sha1Password(password, username));
        Assert.assertEquals(user.getEmail(), email);

        userRepository.delete(user);
    }

    @Test
    public void activeFail () throws Exception {
        String uri = "/active/aaa";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                    .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        assertContent(content, "激活失败!");
    }

    @Test
    public void activeSuccess () throws Exception {
        // 注册
        mvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .param("username", username)
                        .param("password", password)
                        .param("password2", password)
                        .param("email", email)
                        .param("isTest", "1")
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        String uuid = stringRedisTemplate.opsForValue().get("TEST-UUID-ID:");
        String uri = "/active/" + uuid;

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        assertContent(content, "恭喜您, 激活成功!");

        User user = userRepository.findByUsername(username);

        Assert.assertEquals(user.getStatus(), 1);

        userRepository.delete(user);
    }




}
