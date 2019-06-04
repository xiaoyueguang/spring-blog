package com.ray.blog.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String uid = "";
    @Column(nullable = false, unique = true)
    private String username;
    @Column(unique = true)
    private String nickname;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private int status;

    public User() {
        super();
    }
    public User (String username, String nickname, String password, String email) {
        super();
        this.setUid();
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.status = 0;
    }

    public String getUid () {
        return uid;
    }
    public void setUid () {
        if (uid.equals("")) {
            // 为空则设置一个
            this.uid = UUID.randomUUID().toString().replaceAll("-", "");
        }
    }
    public String getUsername () {
        return username;
    }
    public void setUsername (String username) {
        this.username = username;
    }
    public String getNickname () {
        return nickname;
    }
    public void setNickname (String nickname) {
        this.nickname = nickname;
    }
    public String getPassword () {
        return password;
    }
    public void setPassword (String password) {
        this.password = password;
    }
    public String getEmail () {
        return email;
    }
    public void setEmail (String email) {
        this.email = email;
    }
    public int getStatus () {
        return status;
    }

    /**
     * 1 激活 2 禁用 0 待激活
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

}
