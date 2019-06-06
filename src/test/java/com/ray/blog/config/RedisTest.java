package com.ray.blog.config;

import com.ray.blog.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() throws Exception {
        String key = UUID.randomUUID().toString();
        String value = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(key, value);

        Assert.assertEquals(value, stringRedisTemplate.opsForValue().get(key));
    }

    @Test
    public void testUser () throws Exception {
        User user = new User("testusername", "testnickname", "password", "test@test.com");

        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        String key1 = UUID.randomUUID().toString();
        String key2 = UUID.randomUUID().toString();
        operations.set(key1, user);
        operations.set(key2, user, 1, TimeUnit.SECONDS);
        // 利用等待. 使的上面一行代码缓存的内容失效
        Thread.sleep(1000);

        Assert.assertTrue(redisTemplate.hasKey(key1));
        Assert.assertFalse(redisTemplate.hasKey(key2));
    }
}
