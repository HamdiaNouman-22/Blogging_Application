package com.blogapp.bloggingapplication.entities;

import com.google.cloud.spring.data.firestore.Document;
import lombok.Data;


public class Role {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
