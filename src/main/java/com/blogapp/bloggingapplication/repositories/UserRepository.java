package com.blogapp.bloggingapplication.repositories;

import com.blogapp.bloggingapplication.entities.User;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends FirestoreReactiveRepository<User> {
    Optional<User> findByEmailaddress(String username);
}
