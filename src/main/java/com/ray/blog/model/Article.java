package com.ray.blog.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Article implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String title;
    @Column
    private String content;
    @Column(nullable = false)
    private String user_id;

    public Article () {
        super();
    }
    public Article(String title, String content, String user_id) {
        this.title = title;
        this.content = content;
        this.user_id = user_id;
    }

    public String getTitle () {
        return title;
    }
    public void setTitle (String title) {
        this.title = title;
    }
    public String getContent () {
        return content;
    }
    public void setContent (String content) {
        this.content = content;
    }
    public String getUser_id () {
        return user_id;
    }
    public void setUser_id (String user_id) {
        this.user_id = user_id;
    }
}
