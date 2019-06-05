package com.ray.blog.model;

import javax.persistence.*;
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
    private String uid;

    @OneToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid", insertable = false, updatable = false)
    private User user;

    public Article () {
        super();
    }
    public Article(String title, String content, String uid) {
        this.title = title;
        this.content = content;
        this.uid = uid;
    }

    public Long getId () {
        return id;
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
    public String getUid () {
        return uid;
    }
    public void setUid (String uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }
}
