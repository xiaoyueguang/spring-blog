package com.ray.blog.model;

import com.ray.blog.repository.ArticleRepository;
import com.ray.blog.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleRepositoryTests {
    @Autowired
    public ArticleRepository articleRepository;
    @Autowired
    public UserRepository userRepository;

    @Test
    public void test() throws Exception {
        User user = new User("testusername", "testnickname", "password", "test@test.com");
        userRepository.save(user);
        String title = "标题1" + UUID.randomUUID();
        String content = "内容2" + UUID.randomUUID();
        Article article1 = new Article(title, content, user.getUid());

        articleRepository.save(article1);

        Article article2 = articleRepository.findByTitle(title);

        Assert.assertEquals(article2.getContent(), content);
        Assert.assertEquals(article2.getUser().getUid(), user.getUid());

        articleRepository.delete(article2);
        userRepository.delete(user);
    }
}
