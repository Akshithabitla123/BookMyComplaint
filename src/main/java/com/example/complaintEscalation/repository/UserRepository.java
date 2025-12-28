package com.example.complaintEscalation.repository;

import com.example.complaintEscalation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);
}
