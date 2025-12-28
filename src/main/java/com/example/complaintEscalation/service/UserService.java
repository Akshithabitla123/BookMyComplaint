package com.example.complaintEscalation.service;


import com.example.complaintEscalation.model.User;
import com.example.complaintEscalation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    //save user
    public String saveUser(User user){

        if(userRepo.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        if(userRepo.existsByUserName(user.getUserName())){
            throw new RuntimeException("User name already exists");
        }

        userRepo.save(user);
        return "Success";
    }

    //get all users
    public List<User> getAllUsers(){
        List<User> res=userRepo.findAll();
        return res;
    }

    //get user by id
    public User getUserById(int id){
        User u=userRepo.findById(id).orElse(new User());
        return u;
    }





}
