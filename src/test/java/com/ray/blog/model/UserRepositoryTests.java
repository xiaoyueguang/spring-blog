package com.ray.blog.model;

import com.ray.blog.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void test() throws Exception {
        User user = new User("testusername", "testnickname", "password", "test@test.com");
        userRepository.save(user);

        Assert.assertEquals("password", userRepository.findByUsernameOrEmail("testusername", "test@test.com").getPassword());
        Assert.assertNotEquals("uuid", userRepository.findByUsername("testusername").getUid());
        Assert.assertEquals(0, userRepository.findByUsername("testusername").getStatus());

        userRepository.delete(user);
    }
}
