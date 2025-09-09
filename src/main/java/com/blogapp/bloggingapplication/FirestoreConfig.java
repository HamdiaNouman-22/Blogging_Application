package com.blogapp.bloggingapplication;

import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.TransportChannel;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.v1.stub.FirestoreStubSettings;
import com.google.cloud.spring.data.firestore.FirestoreTemplate;
import com.google.cloud.spring.data.firestore.mapping.FirestoreClassMapper;
import com.google.cloud.spring.data.firestore.mapping.FirestoreDefaultClassMapper;
import com.google.firestore.v1.FirestoreGrpc;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.spring.data.firestore.mapping.FirestoreMappingContext;
import io.github.cdimascio.dotenv.Dotenv;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import com.google.cloud.firestore.v1.FirestoreStub;
import com.google.cloud.firestore.v1.stub.FirestoreStub;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Configuration
public class FirestoreConfig {
    private ManagedChannel channel;
    private static final Dotenv dotenv = Dotenv.load();

    //    @Value("${FIREBASE_KEY_JSON}")
//    private String firebaseKeyJson;
    @Bean
    public Firestore firestore() {
        try {
            // String firebaseKeyJson = System.getenv("FIREBASE_KEY_JSON");
            String firebaseKeyJson = dotenv.get("FIREBASE_KEY_JSON").trim().replace("\\n", "\n");
            System.out.println(firebaseKeyJson);
            if (firebaseKeyJson == null || firebaseKeyJson.isEmpty()) {
                throw new RuntimeException("FIREBASE_KEY_JSON environment variable is not set.");
            }
            System.out.println("1");
            ByteArrayInputStream serviceAccountStream =
                    new ByteArrayInputStream(firebaseKeyJson.getBytes(StandardCharsets.UTF_8));
//            BufferedReader reader = new BufferedReader(new InputStreamReader(serviceAccountStream));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
            System.out.println("2");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);
            System.out.println(credentials);
            //      FileInputStream serviceAccount = new FileInputStream("/app/firebasekey.json");
//            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            String projectId = dotenv.get("SPRING_CLOUD_GCP_FIRESTORE_PROJECT_ID");
            System.out.println("Using project ID: " + projectId);
            System.out.println("3");
            if (projectId == null || projectId.isEmpty()) {
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
        } catch (
                IllegalArgumentException e) {
            throw new RuntimeException("Failed to initialize Firestore due to an IllegalArgumentException: " + e.getMessage(), e);
        }
    }}

//    @Bean
//    public FirestoreMappingContext firestoreMappingContext() {
//        return new FirestoreMappingContext();
//    }
//
//    @Bean
//    public FirestoreStub firestoreStub() {
//        channel = ManagedChannelBuilder.forTarget("firestore.googleapis.com")
//                .useTransportSecurity()
//                .build();
//        TransportChannel channelProvider = GrpcTransportChannel.create(channel);
////       FirestoreStubSettings firestoreStub = FirestoreStubSettings.newBuilder()
////                .setTransportChannelProvider(
////                       TransportChannelProvider.of(channelProvider))
////                .build();
//        FirestoreStub firestoreStub=new FirestoreStub();
//       return FirestoreGrpc.newStub(FirestoreStub );
//        //return FirestoreGrpc.newStub(firestoreStub);
//    }
////    @Bean
////    public FirestoreTemplate firestoreTemplate(FirestoreStub firestoreStub, FirestoreMappingContext firestoreMappingContext) {
////        return new FirestoreTemplate(firestoreStub, dotenv.get("SPRING_CLOUD_GCP_FIRESTORE_PROJECT_ID"),
////                new FirestoreDefaultClassMapper(firestoreMappingContext), firestoreMappingContext);
////    }
//
//    @Bean
//    public FirestoreTemplate firestoreTemplate(FirestoreStub firestoreStub,
//                                               FirestoreMappingContext firestoreMappingContext) {
//        return new FirestoreTemplate(firestoreStub,,FirestoreDefaultClassMapper,firestoreMappingContext);
//    }
//
//    @PreDestroy
//    public void shutdown() {
//        if (channel != null) {
//            channel.shutdown();
//        }
//    }
//
//}
//
