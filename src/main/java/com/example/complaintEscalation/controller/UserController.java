package com.example.complaintEscalation.controller;


import com.example.complaintEscalation.model.User;
import com.example.complaintEscalation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

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
    @GetMapping("user/{id}")
    public User getUserById(@PathVariable int id){
        return userService.getUserById(id);
    }
}
