package com.example.complaintEscalation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.complaintEscalation.model.User;

public interface UserRepository extends JpaRepository<User,Integer> {

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);
    User findByEmailAndPass(String email,String pass);
    List<User> findByRole(String role);

}
