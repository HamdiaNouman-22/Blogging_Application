package com.blogapp.bloggingapplication.controllers;

import com.blogapp.bloggingapplication.ErrorResponse;
import com.blogapp.bloggingapplication.Services.FirebaseService;
import com.blogapp.bloggingapplication.Services.UserService;
import com.blogapp.bloggingapplication.exceptions.ResourceNotFoundException;
import com.blogapp.bloggingapplication.payloads.UserDTO;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.hibernate.engine.internal.Collections;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    FirebaseService firebaseService;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/")
    public ResponseEntity<?> createdUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) throws ExecutionException, InterruptedException {
        if (result.hasErrors()) {
            // Extract the first error message (for email format or other validation)
            String errorMessage = result.getFieldError("emailaddress") != null ?
                    result.getFieldError("emailaddress").getDefaultMessage() : "Validation error";

            // Return the custom error response
            return ResponseEntity
                    .badRequest()
                    .body(java.util.Collections.singletonMap("error", errorMessage));
        }
        try{
        UserDTO createdUser=this.userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }
    @PutMapping("/{userid}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable("userid") String userid) throws ExecutionException,InterruptedException{
        UserDTO updateuser = this.userService.updateUser(userDTO, userid);
        return ResponseEntity.ok(updateuser);
    }

    @DeleteMapping("/{userid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> DeleteUser(@PathVariable("userid") String userid) {
        String response=this.userService.deleteUser(userid);
        if(response.contains("does not exists")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        else{
        return new ResponseEntity<>(Map.of("message", "User deleted sucessfully"), HttpStatus.OK);}
    }

    @GetMapping("/")
    @JsonView(UserDTO.PublicView.class)
    public ResponseEntity<List<UserDTO>> getAllUsers() throws ExecutionException,InterruptedException{
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/{userid}")
    @JsonView(UserDTO.PublicView.class)
    public ResponseEntity<Object> getSingleUser(@PathVariable("userid") String userid) throws ExecutionException, InterruptedException{
        try {
            UserDTO userDTO = this.userService.getUserById(userid);
            return ResponseEntity.ok(userDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the user.");
        }
    }
}
