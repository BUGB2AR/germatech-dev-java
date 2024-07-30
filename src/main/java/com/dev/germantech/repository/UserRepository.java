package com.dev.germantech.repository;

import com.dev.germantech.model.User;

import java.util.List;

public interface UserRepository {
    void save(User user);
    User findByCpf(String cpf);
    void update(User user);
    void delete(User user);
    User findById(int id);
    List<User> findAll();
}
