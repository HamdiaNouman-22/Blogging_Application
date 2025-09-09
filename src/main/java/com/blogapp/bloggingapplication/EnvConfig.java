package com.blogapp.bloggingapplication;

import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    public static String getFirebaseKeyJson() {
        return System.getenv("FIREBASE_KEY_JSON");
    }

    public static String getFirestoreProjectId() {
        return System.getenv("SPRING_CLOUD_GCP_FIRESTORE_PROJECT_ID");
    }
}
