package com.ray.blog.service;

public interface PasswordService {
    public String sha1Password (String password, String username);
}
