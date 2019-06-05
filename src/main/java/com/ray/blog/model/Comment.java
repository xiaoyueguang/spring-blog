package com.ray.blog.model;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Long aid;
    @Column(nullable = false)
    private String uid;
    @Column
    private String content;

    @OneToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid", insertable = false, updatable = false)
    private User user;

    public Comment () {
        super();
    }
    public Comment (Long aid, String uid, String content) {
        super();
        this.aid = aid;
        this.uid = uid;
        this.content = content;
    }
    public Long getId () {
        return id;
    }
    public Long getAid() {
        return aid;
    }
    public String getUid () {
        return uid;
    }
    public void setAid (Long aid) {
        this.aid = aid;
    }
    public void setUid (String uid) {
        this.uid = uid;
    }
    public String getContent () {
        return content;
    }
    public void setContent (String content) {
        this.content = content;
    }
    public User getUser () {
        return user;
    }
}
