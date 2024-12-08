package com.example.dailypuzzle.repository;

import com.example.dailypuzzle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> { //to allow CRUD operations

    User findByUsername(String username);
    User findByEmail(String email);
    User findByUsernameAndPassword(String username, String password);


    List<User> findByRole(String role);

    void deleteByUsername(String username);

}
