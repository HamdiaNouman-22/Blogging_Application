package com.blogapp.bloggingapplication.payloads;

import org.antlr.v4.runtime.misc.NotNull;

public class CategoryDTO {
    @NotNull
    String categoryTitle;
    @NotNull
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
