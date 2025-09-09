package com.blogapp.bloggingapplication;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirestoreConfig {

    @Bean
    public Firestore firestore() {
        try {
            // Load Firebase key JSON from Railway environment variable
            String firebaseKeyJson = System.getenv("FIREBASE_KEY_JSON");
            if (firebaseKeyJson == null || firebaseKeyJson.isEmpty()) {
                throw new RuntimeException("FIREBASE_KEY_JSON environment variable is not set.");
            }

            // Replace escaped newlines
            firebaseKeyJson = firebaseKeyJson.trim().replace("\\n", "\n");

            // Load credentials
            ByteArrayInputStream serviceAccountStream =
                    new ByteArrayInputStream(firebaseKeyJson.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);

            // Get project ID from environment
            String projectId = System.getenv("SPRING_CLOUD_GCP_FIRESTORE_PROJECT_ID");
            if (projectId == null || projectId.isEmpty()) {
                throw new IllegalArgumentException("SPRING_CLOUD_GCP_FIRESTORE_PROJECT_ID environment variable must be set");
            }

            // Build Firestore client
            return FirestoreOptions.getDefaultInstance()
                    .toBuilder()
                    .setCredentials(credentials)
                    .setProjectId(projectId)
                    .build()
                    .getService();

        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firestore: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to initialize Firestore due to an IllegalArgumentException: " + e.getMessage(), e);
        }
    }
}
