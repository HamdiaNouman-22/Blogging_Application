package com.blogapp.bloggingapplication.controllers;

import com.blogapp.bloggingapplication.ErrorResponse;
import com.blogapp.bloggingapplication.Services.FirebaseService;
import com.blogapp.bloggingapplication.Services.UserService;
import com.blogapp.bloggingapplication.exceptions.ResourceNotFoundException;
import com.blogapp.bloggingapplication.payloads.UserDTO;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Users", description = "Endpoints for managing users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    FirebaseService firebaseService;
    @Autowired
    ModelMapper modelMapper;
    @Operation(summary = "Create a user", description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(summary = "Update a user", description = "Update an existing user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{userid}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable("userid") String userid) throws ExecutionException,InterruptedException{
        UserDTO updateuser = this.userService.updateUser(userDTO, userid);
        return ResponseEntity.ok(updateuser);
    }
    @Operation(summary = "Delete a user", description = "Delete a user by ID (admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not an admin")
    })
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
    @Operation(summary = "Get all users", description = "Retrieve all users (public view)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping("/")
    @JsonView(UserDTO.PublicView.class)
    public ResponseEntity<List<UserDTO>> getAllUsers() throws ExecutionException,InterruptedException{
        return ResponseEntity.ok(this.userService.getAllUsers());
    }
    @Operation(summary = "Get single user", description = "Retrieve a single user by ID (public view)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
