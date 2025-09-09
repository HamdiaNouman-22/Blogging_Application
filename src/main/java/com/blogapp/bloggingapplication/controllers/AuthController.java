package com.blogapp.bloggingapplication.controllers;

import com.blogapp.bloggingapplication.Services.UserService;
import com.blogapp.bloggingapplication.exceptions.ApiException;
import com.blogapp.bloggingapplication.payloads.JWTAuthRequest;
import com.blogapp.bloggingapplication.payloads.JWTAuthResponse;
import com.blogapp.bloggingapplication.payloads.UserDTO;
import com.blogapp.bloggingapplication.security.JwtTokenHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/")
@Validated
@Tag(name = "Authentication", description = "Endpoints for user login and registration")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenHelper jwtTokenHelper;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Operation(
            summary = "Login user",
            description = "Authenticate user and return JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                            content = @Content(schema = @Schema(implementation = JWTAuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid username or password")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> createToken(@RequestBody JWTAuthRequest jwtAuthRequest) throws Exception {
        this.authenticate(jwtAuthRequest.getUsername(), jwtAuthRequest.getPassword());

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtAuthRequest.getUsername());
        String token = this.jwtTokenHelper.generateToken(userDetails);
        JWTAuthResponse response = new JWTAuthResponse();
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public void authenticate(String username, String password) throws Exception {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            this.authenticationManager.authenticate(authenticationToken);
        } catch (
                ApiException e) {
            throw new ApiException("Invalid username or password");
        }
    }
    @Operation(
            summary = "Register new user",
            description = "Create a new user account",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Validation errors occurred")
            }
    )
    @PostMapping("/register")
    ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO,BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            logger.error("Validation errors: {}", errors);

            return ResponseEntity.badRequest().body(errors);
        }
        UserDTO registeruser=this.userService.createUser(userDTO);
        logger.info("Received registration request: {}", userDTO);

        logger.info("Validation passed. Proceeding with user creation...");
        return new ResponseEntity<>(registeruser, HttpStatus.CREATED);
    }
}
