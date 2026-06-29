package com.example.complaintEscalation.controller;


import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.complaintEscalation.model.User;
import com.example.complaintEscalation.service.UserService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    //create user
    @PostMapping
    public void createUser(@RequestBody User user){
        userService.saveUser(user);
    }

    //Get all users
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    //Get user by id
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable int id){
        return userService.getUserById(id);
    }

    //Login user by email and password
    @PostMapping("/user/login")
    public User getUserByEmailAndPass(@RequestBody Map<String,String> body){
        String email= body.get("email");
        String pass=body.get("pass");
        return userService.login(email, pass);
    }
}
