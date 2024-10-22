package com.blogapp.bloggingapplication;

import com.google.cloud.spring.data.firestore.repository.config.EnableReactiveFirestoreRepositories;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.io.FileInputStream;
import com.google.auth.oauth2.GoogleCredentials;

@SpringBootApplication
@EnableReactiveFirestoreRepositories
public class BloggingApplication implements CommandLineRunner {
    public static void main(String[] args) {

        SpringApplication.run(BloggingApplication.class, args);

    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args){
        String jsonPath = System.getenv("FIREBASE_KEY_JSON");
        try (FileInputStream serviceAccount = new FileInputStream(jsonPath)) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            System.out.println("Firestore initialized successfully.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
