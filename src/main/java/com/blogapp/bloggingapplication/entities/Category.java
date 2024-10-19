package com.blogapp.bloggingapplication.entities;

import com.google.cloud.spring.data.firestore.Document;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collectionName = "categories")
public class Category {
    @Id
    Integer categoryId;
    String categoryTitle;
    String categoryDescription;
    //with cascade if we are adding or removing parent we want child to get added or removed with them
    List<String> postids=new ArrayList<>();

    public Category() {
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
}
