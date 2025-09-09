package com.blogapp.bloggingapplication;

import com.google.cloud.spring.data.firestore.repository.config.EnableReactiveFirestoreRepositories;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
                System.err.println("❌ FIREBASE_KEY_JSON is missing!");
            } else {
                System.out.println("✅ FIREBASE_KEY_JSON found, length = " + firebaseKeyJson.length());
            }

            // Fix escaped newlines (\n → actual newlines)
            if (firebaseKeyJson != null) {
                firebaseKeyJson = firebaseKeyJson.replace("\\n", "\n");

                // Write to a temp file
                Path tempFile = Files.createTempFile("firebase-", ".json");
                Files.writeString(tempFile, firebaseKeyJson, StandardOpenOption.WRITE);

                // Set system property so Spring Boot picks it up
                System.setProperty("FIREBASE_KEY_JSON_FILE_PATH", tempFile.toAbsolutePath().toString());

                System.out.println("📂 Firebase credentials written to: " + tempFile);
            }

            SpringApplication.run(BloggingApplication.class, args);

        } catch (Exception e) {
            System.err.println("🔥 Application failed to start due to an error:");
            e.printStackTrace(); // show full cause in Railway logs
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) {
        System.out.println("🚀 Firestore should be initialized by Spring Boot now");
    }
}
