package com.blogapp.bloggingapplication.entities;

import jakarta.persistence.*;

@Entity
@Table
public class Comment {
    @Id
    private String id;
    private String content;

    @ManyToOne
     private Post post;
    @ManyToOne
    private User user;

    public Comment() {
        id="0";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
