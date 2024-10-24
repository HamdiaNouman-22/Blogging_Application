//package com.blogapp.bloggingapplication.Services;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import jakarta.annotation.PostConstruct;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//@Service
//public class FirebaseInitialize {
//    private static Logger logger = LoggerFactory.getLogger(FirebaseInitialize.class);
//
//    @PostConstruct
//    public void initialize() {
//        try {
//            FileInputStream serviceAccount = new FileInputStream("src/main/java/com/blogapp/bloggingapplication/firebasekey.json");
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setStorageBucket("blogging-application-d96d3.appspot.com")
////                    .setDatabaseUrl("https://blogging-application-d96d3.firebaseio.com")
//                    .build();
//            if (FirebaseApp.getApps().isEmpty()) {
//                FirebaseApp.initializeApp(options);
//                logger.info("Firebase successfully initialized.");
//            }
//        } catch (
//                IOException e) {
//            logger.error("Failed to initialize Firebase: {}", e.getMessage());
//        } catch (
//                Exception e) {
//            logger.error("Unexpected error occurred while initializing Firebase: {}", e.getMessage());
//        }
////            FirebaseApp.initializeApp(options);
////        }catch(Exception e){
////            logger.error("Failed to initialize firebase "+ e);
////        }
//    }
//}
package com.blogapp.bloggingapplication.Services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FirebaseInitialize {
    private static Logger logger = LoggerFactory.getLogger(FirebaseInitialize.class);

    @PostConstruct
    public void initialize() {
        try {
            String firebaseKeyJson = System.getenv("FIREBASE_KEY_JSON");
            System.out.println(firebaseKeyJson);

            if (firebaseKeyJson == null || firebaseKeyJson.isEmpty()) {
                throw new IOException("FIREBASE_KEY_JSON environment variable is missing or empty.");
            }
            ByteArrayInputStream serviceAccount = new ByteArrayInputStream(firebaseKeyJson.getBytes());
            // Update the path to point to the correct location in the Docker container
           // FileInputStream serviceAccount = new FileInputStream("/app/firebasekey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("blogging-application-d96d3.appspot.com")
                    // Uncomment if you need to set the database URL
                    //.setDatabaseUrl("https://blogging-application-d96d3.firebaseio.com")
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("Firebase successfully initialized.");
            }
        } catch (IOException e) {
            logger.error("Failed to initialize Firebase: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while initializing Firebase: {}", e.getMessage());
        }
    }
}
