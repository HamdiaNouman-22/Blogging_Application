package com.blogapp.bloggingapplication.Services;

import com.blogapp.bloggingapplication.payloads.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserService {
    UserDTO createUser(UserDTO user);
    UserDTO updateUser(UserDTO user,String id) throws ExecutionException, InterruptedException;
    UserDTO getUserById(String id) throws ExecutionException, InterruptedException;
    List<UserDTO> getAllUsers() throws ExecutionException,InterruptedException;
    String deleteUser(String id);

}
