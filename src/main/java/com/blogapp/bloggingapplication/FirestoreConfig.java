package com.blogapp.bloggingapplication;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
//import com.google.cloud.spring.data.firestore.FirestoreMappingContext;
import com.google.cloud.spring.data.firestore.FirestoreTemplate;
import com.google.cloud.spring.data.firestore.mapping.FirestoreDefaultClassMapper;
import com.google.firestore.v1.FirestoreGrpc;
import com.google.firestore.v1.FirestoreGrpc.FirestoreStub;
import com.google.cloud.firestore.FirestoreOptions;
//import com.google.cloud.firestore.Firestore;
//import com.google.cloud.firestore.FirestoreOptions;
//import com.google.cloud.spring.data.firestore.FirestoreTemplate;
import com.google.cloud.spring.data.firestore.mapping.FirestoreMappingContext;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
//
//import com.google.firestore.v1.FirestoreGrpc;

@Configuration
public class FirestoreConfig{
    private ManagedChannel channel;
    @Bean
    public Firestore firestore() {
        try {
            FileInputStream serviceAccount = new FileInputStream("/app/firebasekey.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            String projectId = System.getenv("SPRING_CLOUD_GCP_FIRESTORE_PROJECT_ID");
            System.out.println("Using project ID: " + projectId);
            if (projectId == null) {
                throw new IllegalArgumentException("Environment variable SPRING_CLOUD_GCP_FIRESTORE_PROJECT_ID must be set");
            }
            return FirestoreOptions.getDefaultInstance()
                    .toBuilder()
                    .setCredentials(credentials)
                    .setProjectId(projectId)
                    .build()
                    .getService();
        } catch (
                IOException e) {
            throw new RuntimeException("Failed to initialize Firestore: " + e.getMessage(), e);
        }
    }

    @Bean
    public FirestoreMappingContext firestoreMappingContext() {
        return new FirestoreMappingContext();
    }

    @Bean
    public FirestoreStub firestoreStub() {
        channel = ManagedChannelBuilder.forTarget("firestore.googleapis.com")
                .build();
        return FirestoreGrpc.newStub(channel);
    }

    @Bean
    public FirestoreTemplate firestoreTemplate(FirestoreStub firestoreStub, FirestoreMappingContext firestoreMappingContext) {
        return new FirestoreTemplate(firestoreStub, "blogging-application-d96d3", new FirestoreDefaultClassMapper(firestoreMappingContext), firestoreMappingContext);
    }
    @PreDestroy
    public void shutdown() {
        if (channel != null) {
            channel.shutdown();
        }
    }

}

