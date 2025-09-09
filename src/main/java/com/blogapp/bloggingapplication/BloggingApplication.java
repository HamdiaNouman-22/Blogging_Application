package com.blogapp.bloggingapplication;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.spring.data.firestore.repository.config.EnableReactiveFirestoreRepositories;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

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
    public void run(String... args) {
        // Read Firebase JSON from environment variable
        String firebaseKeyJson = System.getenv("FIREBASE_KEY_JSON");
        if (firebaseKeyJson == null || firebaseKeyJson.isEmpty()) {
            throw new RuntimeException("FIREBASE_KEY_JSON environment variable is not set.");
        }

        // Replace escaped newlines (\n) with actual newlines
        firebaseKeyJson = firebaseKeyJson.replace("\\n", "\n");

        try (ByteArrayInputStream serviceAccount =
                     new ByteArrayInputStream(firebaseKeyJson.getBytes(StandardCharsets.UTF_8))) {

            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            System.out.println("Firestore initialized successfully with credentials: " + credentials);

        } catch (Exception e) {
            System.err.println("Error initializing Firestore: " + e.getMessage());
        }
    }
}
