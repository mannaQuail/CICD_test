package com.hacker.hacker.repository;

import com.hacker.hacker.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<Users, Long>{
    Users findByUserId(int userId);
}
