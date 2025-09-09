package com.blogapp.bloggingapplication;

import java.io.FileReader;

public class FirebaseJsonTest {
    public static void main(String[] args) {
        String jsonPath = "C:\\Users\\DELL\\IdeaProjects\\blogging-application\\src\\main\\java\\com\\blogapp\\bloggingapplication\\firebasekey.json";
        try (FileReader reader = new FileReader(jsonPath)) {
            System.out.println("JSON loaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
