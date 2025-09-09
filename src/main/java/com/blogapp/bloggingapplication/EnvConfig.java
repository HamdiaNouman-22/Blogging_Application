package com.blogapp.bloggingapplication;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    private static final Dotenv dotenv=Dotenv.load();
    public static String getFirebaseKeyJson(){
        return dotenv.get("FIREBASE_KEY_JSON");
    }
}
