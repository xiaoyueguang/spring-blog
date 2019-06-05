package com.ray.blog.model;

import com.ray.blog.repository.CommentRepository;
import com.ray.blog.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentRepositoryTests {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void test() throws Exception {
        User user = new User("testusername", "testnickname", "password", "test@test.com");
        userRepository.save(user);

        String content = "test-CONTENT";

        Comment comment1 = new Comment(1L, user.getUid(), content);

        commentRepository.save(comment1);

        Comment comment2 = commentRepository.findByAidAndUidAndContent(1L, user.getUid(), content);

        Assert.assertEquals(user.getNickname(), comment2.getUser().getNickname());
        Assert.assertEquals(content, comment2.getContent());

        commentRepository.delete(comment1);
        userRepository.delete(user);
    }
}
