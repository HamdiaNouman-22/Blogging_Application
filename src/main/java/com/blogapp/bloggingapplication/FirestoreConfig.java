package com.blogapp.bloggingapplication;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.io.FileNotFoundException;

@Configuration
public class FirestoreConfig {

    @Bean
    public Firestore firestore() {
        try {
            // Load Firebase key JSON from resources folder
            InputStream serviceAccount = getClass().getClassLoader()
                    .getResourceAsStream("firebasekey.json"); // ensure file is in src/main/resources
            if (serviceAccount == null) {
                throw new FileNotFoundException("firebasekey.json not found in resources folder");
            }

            // Load credentials
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

            // Build Firestore client
            FirestoreOptions options = FirestoreOptions.newBuilder()
                    .setCredentials(credentials)
                    .build();

            return options.getService();

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Firestore", e);
        }
    }
}
