package com.blogapp.bloggingapplication;

import com.blogapp.bloggingapplication.entities.Role;
import com.blogapp.bloggingapplication.entities.User;
import com.blogapp.bloggingapplication.repositories.RoleRepository;
import com.blogapp.bloggingapplication.security.CustomerDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class BloggingApplication implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;
    public static void main(String[] args) {
        SpringApplication.run(BloggingApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) throws Exception {
            Role role1=new Role();
            role1.setId("1");
            role1.setName("ADMIN_USER");
            Role role2=new Role();
            role2.setId("2");
            role2.setName("NORMAL_USER");
            List<Role> roles=List.of(role1,role2);
            List<Role>result=this.roleRepository.saveAll(roles);
    }
}
