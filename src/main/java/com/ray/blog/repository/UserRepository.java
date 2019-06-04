package com.ray.blog.repository;

import com.ray.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameOrEmail (String username, String email);
    User findByUsernameAndPassword (String username, String password);
    User findByUsername (String username);
    User findByEmail (String email);
    User findByUid (String uid);
}
