package com.blogapp.bloggingapplication.Services.impl;

import com.blogapp.bloggingapplication.Constants;
import com.blogapp.bloggingapplication.Services.FirebaseService;
import com.blogapp.bloggingapplication.Services.UserService;
import com.blogapp.bloggingapplication.entities.User;
import com.blogapp.bloggingapplication.payloads.UserDTO;
import com.blogapp.bloggingapplication.repositories.UserRepository;
import com.blogapp.bloggingapplication.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    FirebaseService firebaseService;
    private static Logger logger = LoggerFactory.getLogger(UserServiceImplementation.class);


    @Override
    public UserDTO createUser(UserDTO userDTO) {

        String password = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        userDTO.setPassword(encodedPassword);
        if (userDTO.getRoles() == null || userDTO.getRoles().isEmpty()) {
            List<String> defaultRoles = List.of(Constants.NORMAL_USER);
            userDTO.setRoles(defaultRoles);
        }
        User user = this.dtotouser(userDTO);
        try {
            String id = user.getEmailaddress();
            String updateTime =  firebaseService.saveDocument("users", id, user);
            System.out.println("User saved successfully with update time: " + updateTime);
        } catch (
                ExecutionException |
                InterruptedException e) {
            logger.error("Error saving user to document " + e);
        }
        return this.usertodto(user);
    }

public UserDTO updateUser(UserDTO userDTO,String userId) throws ExecutionException, InterruptedException {
    Map<String, Object> updates = new HashMap<>();
    if (userDTO.getEmailaddress() != null) {
        updates.put("emailaddress", userDTO.getEmailaddress());
    }
    if (userDTO.getAbout() != null) {
        updates.put("about", userDTO.getAbout());
    }

    firebaseService.updateDocument("users", userId, updates);

    UserDTO updatedUser = firebaseService.getDocument("users", userId, UserDTO.class);
    return updatedUser;
}
    @Override
    public String deleteUser(String id) {
        return (firebaseService.deleteDocument("users",id));
    }

    @Override
    public UserDTO getUserById(String id) throws ExecutionException, InterruptedException{
        User user=firebaseService.getDocument("users",id,User.class);
        if (user == null) {
            throw new ResourceNotFoundException("User with id: " + id + " not found");
        }

        return this.usertodto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() throws ExecutionException,InterruptedException{
        List<User> users=this.firebaseService.getAllDocument("users",User.class);
        List<UserDTO> userDTOS = users.stream().map(user -> this.usertodto(user)).collect(Collectors.toList());
        return userDTOS;
    }

    public User dtotouser(UserDTO userDTO) {
        User user = this.modelMapper.map(userDTO, User.class);
        return user;
    }

    public UserDTO usertodto(User user) {
        UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

}
