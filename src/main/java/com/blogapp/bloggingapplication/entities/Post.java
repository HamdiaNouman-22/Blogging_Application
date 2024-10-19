package com.blogapp.bloggingapplication.entities;

import com.google.cloud.spring.data.firestore.Document;
import lombok.Data;

import java.util.*;

@Data
@Document(collectionName="posts")
public class Post {

    private String postId;
    private String title;
    private String content;
    private String imageName;
    private Date addedDate;
    private String titleTokens;
    private String categoryId;
    private String userId;
//    private Category category;
//    private User user;


    public Post() {
        postId="0";
    }
    public String getTitleTokens() {
        return titleTokens;
    }

    public void setTitleTokens(String titleTokens) {
        this.titleTokens = titleTokens;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

//    public Category getCategory() {
//        return category;
//    }
//
//    public void setCategory(Category category) {
//        this.category = category;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
