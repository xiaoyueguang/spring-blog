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
public class UserControllerTests extends AbstractTest {
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
    public void indexWithLgout () throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("uid", "");
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get("/user")
                        .session(session)
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        Assert.assertEquals(mvcResult.getResponse().getStatus(), 302);
    }
    @Test
    public void indexWithPage1 () throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("uid", user.getUid());

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get("/user")
                        .param("page", "0")
                        .session(session)
                        .accept(MediaType.TEXT_HTML_VALUE)
        ).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertContent(content, "<h2>昵称: <span>" + nickname + "</span></h2>");
    }
}
