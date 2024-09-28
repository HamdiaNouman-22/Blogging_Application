package com.blogapp.bloggingapplication.controllers;

import com.blogapp.bloggingapplication.Services.UserService;
import com.blogapp.bloggingapplication.exceptions.ApiException;
import com.blogapp.bloggingapplication.payloads.JWTAuthRequest;
import com.blogapp.bloggingapplication.payloads.JWTAuthResponse;
import com.blogapp.bloggingapplication.payloads.UserDTO;
import com.blogapp.bloggingapplication.security.JwtTokenHelper;
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

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> createToken(@RequestBody JWTAuthRequest jwtAuthRequest) throws Exception {
        System.out.println(jwtAuthRequest.getUsername() + " " + jwtAuthRequest.getPassword());
        this.authenticate(jwtAuthRequest.getUsername(), jwtAuthRequest.getPassword());

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtAuthRequest.getUsername());
        System.out.println(userDetails);
        String token = this.jwtTokenHelper.generateToken(userDetails);
        JWTAuthResponse response = new JWTAuthResponse();
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public void authenticate(String username, String password) throws Exception {
        System.out.println("1");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        System.out.println(authenticationToken);
        try {
            this.authenticationManager.authenticate(authenticationToken);
            System.out.println("2");
        } catch (
                ApiException e) {
            System.out.println("Invalid Details");
            throw new ApiException("Invalid username or password");
        }
    }

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
