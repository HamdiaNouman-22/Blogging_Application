package com.blogapp.bloggingapplication.repositories;

import com.blogapp.bloggingapplication.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
}
