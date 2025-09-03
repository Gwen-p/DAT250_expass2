package com.example.expass2.controller;
import com.example.expass2.model.User;
import com.example.expass2.service.PollManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private final PollManager pollManager;

    // Inyección de dependencias
    public UserController(PollManager _pollManager) {
        this.pollManager = _pollManager;
    }

    // ✅ Crear un usuario
    @PostMapping
    public User createUser(@RequestBody User user) {
        return pollManager.addUser(user);
    }

    // ✅ Listar todos los usuarios
    @GetMapping
    public Collection<User> getAllUsers() {
        return pollManager.getUsers();
    }

    // ✅ Obtener un usuario específico por id
    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return pollManager.getUser(id);
    }

    // ✅ Eliminar un usuario
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        pollManager.deleteUser(id);
    }
}
