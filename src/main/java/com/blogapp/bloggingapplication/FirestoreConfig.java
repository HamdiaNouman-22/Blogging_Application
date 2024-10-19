package com.blogapp.bloggingapplication;
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
//
//import com.google.firestore.v1.FirestoreGrpc;

@Configuration
public class FirestoreConfig{
    private ManagedChannel channel;
    @Bean
    public Firestore firestore() {
        // Initialize Firestore using Application Default Credentials
        return FirestoreOptions.getDefaultInstance().getService();
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
//    @Bean
//    public Firestore firestore() {
//        // Initialize Firestore using Application Default Credentials
//        return FirestoreOptions.getDefaultInstance().getService();
//    }
//    @Bean
//    public FirestoreMappingContext firestoreMappingContext() {
//        return new FirestoreMappingContext();
//    }
//    @Bean
//    public FirestoreGrpc.FirestoreStub firestoreStub(Firestore firestore) {
//        // Assuming you have a method to create FirestoreStub from Firestore
//        // If not, you will need to create it based on your requirements
//        return firestore.getFirestore(); // Adjust as necessary
//    }
//
//    @Bean
//    public FirestoreTemplate firestoreTemplate(FirestoreGrpc.FirestoreStub, FirestoreMappingContext firestoreMappingContext) {
//        return new FirestoreTemplate()
//    }
//    @Bean
//    public FirestoreTemplate firestoreTemplate(Firestore firestore, FirestoreMappingContext firestoreMappingContext) {
//        FirestoreGrpc.FirestoreStub firestoreStub = firestore.getFirestore();
//        return new FirestoreTemplate(firestoreStub,"blogging-application-d96d3",null, firestoreMappingContext);
//    }
//    @Bean
//    public Firestore firestore() throws IOException {
//        // Specify the path to the credentials file
//        String credentialsPath = "C:\\Users\\DELL\\IdeaProjects\\blogging-application\\src\\main\\java\\com\\blogapp\\bloggingapplication\\firebasekey.json";
//
//        // Create a FileInputStream from the path
//        FileInputStream serviceAccountStream = new FileInputStream(credentialsPath);
//
//        // Build FirestoreOptions using the service account credentials
//        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
//                .setProjectId("blogging-application-d96d3") // Your Firestore project ID
//                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream)) // Load credentials from the file
//                .build();
//
//        // Return the Firestore service
//        return firestoreOptions.getService();
//    }
//    @Bean
//    public FirestoreTemplate firestoreTemplate(Firestore firestore) {
//        return new FirestoreTemplate(firestore);
//    }
//@Bean
//public FirestoreStub firestoreStub() {
//    ManagedChannel channel = ManagedChannelBuilder.forTarget("firestore.googleapis.com")
//            .build();
//    return FirestoreGrpc.newStub(channel);
//}
//
//    @Bean
//    public FirestoreTemplate firestoreTemplate(FirestoreStub firestoreStub) {
//        return new FirestoreTemplate(firestoreStub, "blogging-application-d96d3", new FirestoreClassMapper());
//    }

//    @Bean
//public Firestore firestore() throws IOException {
//    String credentialsPath = "C:\\Users\\DELL\\IdeaProjects\\blogging-application\\src\\main\\java\\com\\blogapp\\bloggingapplication\\firebasekey.json";
//    GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
//
//    FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
//            .setCredentials(credentials)
//            .setProjectId("blogging-application-d96d3")
//            .build();
//
//    return firestoreOptions.getService();
//}
//
//    @Bean
//    public FirestoreTemplate firestoreTemplate(Firestore firestore, FirestoreMappingContext firestoreMappingContext) {
//        return new FirestoreTemplate(firestore, firestoreMappingContext);
//    }
//    @Bean
//    public FirestoreStub firestoreStub() throws IOException {
//        String credentialsPath = "C:\\Users\\DELL\\IdeaProjects\\blogging-application\\src\\main\\java\\com\\blogapp\\bloggingapplication\\firebasekey.json";
//        FileInputStream serviceAccountStream = new FileInputStream(credentialsPath);
//
//        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);
//        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080") // Or your Firestore endpoint
//                .usePlaintext()
//                .build();
//
//        return FirestoreGrpc.newStub(channel).withInterceptors(credentials);
//    }
//
//    @Bean
//    public FirestoreTemplate firestoreTemplate(FirestoreStub firestoreStub, FirestoreMappingContext firestoreMappingContext) {
//        return new FirestoreTemplate(firestoreStub, "blogging-application-d96d3", new FirestoreClassMapper(), firestoreMappingContext);
//    }
//    @Bean
//    public Firestore firestore() {
//        // Create FirestoreOptions without the emulator settings
//        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
//                .setProjectId("blogging-application-d96d3")
//                .setCredentials(GoogleCredentials.fromFile(new File("C:\\Users\\DELL\\IdeaProjects\\blogging-application\\src\\main\\java\\com\\blogapp\\bloggingapplication\\firebasekey.json")))
//                .build();
//
//        // Return the Firestore service
//        return firestoreOptions.getService();
//    }
//    @Bean
//    public Firestore firestore(){
//        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
//               .setProjectId("blogging-application-d96d3")
//                .setEmulatorHost("localhost:8080")
//                .build();
//        return firestoreOptions.getService();
//    }
////    @Bean
////    public FirestoreTemplate firestoreTemplate(Firestore firestore) {
////
////        return new FirestoreTemplate(firestore);
////    }
//
////    @Bean
////    public FirestoreTemplate firestoreTemplate(Firestore firestore) {
////        // Check if we are using FirestoreStub
////        if (firestore instanceof FirestoreStub) {
////            // Create and return FirestoreTemplate for emulator
////            return new FirestoreTemplate((FirestoreStub) firestore);
////        } else {
////            // Create and return FirestoreTemplate for actual Firestore
////            return new FirestoreTemplate(firestore);
////        }
////    }
//@Bean
//public FirestoreTemplate firestoreTemplate(Firestore firestore) {
//    // Check if we're using the Firestore emulator
//    if (firestore instanceof Firestore) {
//        // Create FirestoreStubSettings for emulator
//        FirestoreStubSettings settings = FirestoreStubSettings.newBuilder()
//                .setEndpoint("localhost:8080")
//                .build();
//
//        FirestoreStub firestoreStub = FirestoreGrpc.newStub(settings);  // Create FirestoreStub for emulator
//
//        // Return FirestoreTemplate with FirestoreStub
//        return new FirestoreTemplate(firestoreStub);
//    }
//
//    // Create and return FirestoreTemplate for the actual Firestore
//    return new FirestoreTemplate(firestore);
//}
//@Bean
//public FirestoreStub firestore() {
//    // Create a gRPC channel to connect to the Firestore emulator
//    ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
//            .usePlaintext()
//            .build();
//    return FirestoreGrpc.newStub(channel);
//}
//
//    @Bean
//    public FirestoreTemplate firestoreTemplate(FirestoreStub firestoreStub) {
//        return new FirestoreTemplate(firestoreStub);
//    }
}

