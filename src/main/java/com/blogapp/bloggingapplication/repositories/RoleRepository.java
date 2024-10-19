package com.blogapp.bloggingapplication.repositories;

import com.blogapp.bloggingapplication.entities.Role;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends FirestoreReactiveRepository<Role> {
}
