package com.dev.germantech.controller;

import com.dev.germantech.model.User;
import com.dev.germantech.service.UserService;
import java.util.List;

public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void addUser(String name, String cpf, String email, String phone, String password) {
        User user = new User(name, cpf, email, phone, password);
        userService.save(user);
    }

    public void updateUser(User user) {
        userService.update(user);
    }

    public User findById(int id) {

        return userService.findById(id);
    }

    public void deleteUserById(int id) {
        userService.deleteById(id);
    }


    public User findByCpf(String cpf) {
        return userService.findByCpf(cpf);
    }

    public List<User> listUsers() {
        return userService.findAll();
    }
}
