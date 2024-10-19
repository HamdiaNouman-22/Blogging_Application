package com.blogapp.bloggingapplication.payloads;


public class CategoryDTO {
    String categoryTitle;
    String categoryDescription;
    public CategoryDTO() {
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
