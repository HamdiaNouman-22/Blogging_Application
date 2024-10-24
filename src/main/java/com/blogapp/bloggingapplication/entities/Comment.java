package com.blogapp.bloggingapplication.entities;

import com.google.cloud.spring.data.firestore.Document;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Document(collectionName = "comments")
public class Comment {
    @Id
    private String id;
    private String content;
private String postId;
    private String userId;
//     private Post post;
//    private User user;

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

//    public Post getPost() {
//        return post;
//    }
//
//    public void setPost(Post post) {
//        this.post = post;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
