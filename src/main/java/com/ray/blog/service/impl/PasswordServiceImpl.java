package com.ray.blog.service.impl;

import com.ray.blog.config.BlogProperties;
import com.ray.blog.service.PasswordService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordServiceImpl implements PasswordService {
    @Autowired
    private BlogProperties blogProperties;

    /*
     * 密码加密:
     * md5(md5(sha1(MD5(用户+MD5(密码)).substr(2)+salt).substr(2)))
     * */
    public String sha1Password (String password, String username) {
        return DigestUtils.md5Hex(
                DigestUtils.md5Hex(
                        DigestUtils.sha1Hex(
                                DigestUtils.md5Hex(username + DigestUtils.md5Hex(password)).substring(4) + blogProperties.getSalt()
                        ).substring(2)
                )
        );
    }
}
