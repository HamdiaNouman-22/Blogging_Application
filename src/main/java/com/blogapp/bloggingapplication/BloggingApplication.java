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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@SpringBootApplication
@EnableReactiveFirestoreRepositories
public class BloggingApplication implements CommandLineRunner {

    public static void main(String[] args) {
        try {
            // Get Firebase JSON from env var
            String firebaseKeyJson = System.getenv("FIREBASE_KEY_JSON");
            if (firebaseKeyJson == null || firebaseKeyJson.isEmpty()) {
                throw new RuntimeException("FIREBASE_KEY_JSON environment variable is not set.");
            }

            // Fix escaped newlines (\n → actual newlines)
            firebaseKeyJson = firebaseKeyJson.replace("\\n", "\n");

            // Write to a temp file
            Path tempFile = Files.createTempFile("firebase-", ".json");
            Files.writeString(tempFile, firebaseKeyJson, StandardOpenOption.WRITE);

            // Set system property so Spring Boot picks it up
            System.setProperty("FIREBASE_KEY_JSON_FILE_PATH", tempFile.toAbsolutePath().toString());

            System.out.println("Firebase credentials written to: " + tempFile);

        } catch (Exception e) {
            throw new RuntimeException("Failed to write Firebase credentials file", e);
        }

        SpringApplication.run(BloggingApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) {
        System.out.println("Firestore should be initialized by Spring Boot now 🚀");
    }
}
