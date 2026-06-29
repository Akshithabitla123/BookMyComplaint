package com.example.complaintEscalation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.complaintEscalation.exceptions.InvalidRequestException;
import com.example.complaintEscalation.model.User;
import com.example.complaintEscalation.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;

    //save user
    public String saveUser(User user){

        if(userRepo.existsByEmail(user.getEmail())){
            throw new InvalidRequestException("Email already exists");
        }

        if(userRepo.existsByUserName(user.getUserName())){
            throw new InvalidRequestException("User name already exists");
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


    public User login(String email, String pass) {
        return userRepo.findByEmailAndPass(email,pass);
    }



}
