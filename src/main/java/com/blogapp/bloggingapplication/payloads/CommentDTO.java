package com.blogapp.bloggingapplication.payloads;

public class CommentDTO {
    String id;
    String content;

    public CommentDTO() {
        id="0";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
