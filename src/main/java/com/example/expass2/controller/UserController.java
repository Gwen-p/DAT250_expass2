package com.example.expass2.controller;
import com.example.expass2.model.User;
import com.example.expass2.service.PollManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private final PollManager pollManager;

    public UserController(PollManager _pollManager) {
        this.pollManager = _pollManager;
    }

    // Create a user
    @PostMapping
    public User createUser(@RequestBody User user) {
        if (pollManager.getUser(user.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User already exists");
        }
        return pollManager.addUser(user);
    }

    // Obtain the users list
    @GetMapping
    public Collection<User> getAllUsers() {
        return pollManager.getUsers();
    }

    // Obtain a user by id
    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        if (pollManager.getUser(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return pollManager.getUser(id);
    }

    // Delete a user
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        if (pollManager.getUser(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        pollManager.deleteUser(id);
    }
}
