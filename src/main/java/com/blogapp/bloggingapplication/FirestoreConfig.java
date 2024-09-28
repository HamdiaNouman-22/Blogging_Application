package com.blogapp.bloggingapplication;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirestoreConfig{
    @Bean
    public Firestore firestore(){
        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                .setProjectId("blogging-application-d96d3")
                .setEmulatorHost("localhost:8080")
                .build();
        return firestoreOptions.getService();
    }
}

