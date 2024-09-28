package com.blogapp.bloggingapplication;

import com.blogapp.bloggingapplication.security.CustomerDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CustomerDetailsClassTest {
    @Autowired
    private CustomerDetailsService customerDetailsService;
    @Test
    public void loadByUsernameTest(){
        String emailAddress="laiba@gmail.com";
        UserDetails userDetails=customerDetailsService.loadUserByUsername(emailAddress);
        System.out.println(userDetails);
        assertNotNull(userDetails);
        assertEquals(emailAddress,userDetails.getUsername());
    }

}
