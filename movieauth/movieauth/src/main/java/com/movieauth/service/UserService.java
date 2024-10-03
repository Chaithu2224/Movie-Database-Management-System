package com.movieauth.service;

import java.util.Optional;

import com.movieauth.entity.User;

public interface UserService {

    public User registerUser(User user);

    public Optional<User> loginUser(String email, String password);

    boolean checkIfEmailExists(String email);

}