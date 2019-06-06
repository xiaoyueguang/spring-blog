package com.ray.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BlogProperties {
    @Value("${com.ray.blog.title}")
    private String title;
    @Value("${com.ray.blog.salt}")
    private String salt;
    @Value("${com.ray.blog.host}")
    private String host;
    @Value("${com.ray.test.email1}")
    private String testEmail1;
    @Value("${com.ray.test.email2}")
    private String testEmail2;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHost () {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
    public String getTestEmail () {
        return testEmail1;
    }
    public String getTestEmail2 () {
        return testEmail2;
    }
}
