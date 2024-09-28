package com.blogapp.bloggingapplication.security;

import com.blogapp.bloggingapplication.Services.FirebaseService;
import com.blogapp.bloggingapplication.exceptions.ResourceNotFoundException;
import com.blogapp.bloggingapplication.repositories.UserRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.blogapp.bloggingapplication.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Firestore firestore;
    @Autowired
    private FirebaseService firebaseService;
private static Logger logger= LoggerFactory.getLogger(CustomerDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String emailaddress) {
        User user;
        try{
        user=firebaseService.getDocument("users",emailaddress,User.class);
        }catch (ExecutionException|InterruptedException e){
            throw new UsernameNotFoundException("Error retrieving user from Firestore", e);
        } if (user == null) {
            throw new UsernameNotFoundException("User with email " + emailaddress + " not found");
        }
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getEmailaddress(), user.getPassword(), authorities);
//        String msg=emailaddress+" not found ";
//        ApiFuture<DocumentSnapshot> future=firestore.collection("users").document(emailaddress).get();
//        try{DocumentSnapshot document=future.get();
//        if(document.exists()){
//            String extractedEmailAddress=document.getString("emailaddress");
//            String password=document.getString("password");
////            List<String> authorityList=(List<String>)document.get("authorities");
//            List<String> roleList = (List<String>) document.get("roles");
//            List<GrantedAuthority> authorities=roleList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//            UserDetails user=new org.springframework.security.core.userdetails.User(extractedEmailAddress,password,authorities);
////            System.out.println(user);
//            logger.info("loaded detail: "+user);
//            return user;
//
//        }
//        else{
//            throw new UsernameNotFoundException(msg);
//        }}catch (ExecutionException | InterruptedException e){
//            throw new UsernameNotFoundException(msg);
//        }

    }
}
