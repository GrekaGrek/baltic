package com.air.baltic.service;

import com.air.baltic.model.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> fetchAllUsers();
    void addUser(UserDTO user);
    Optional<UserDTO> findUserById(Long id);
    UserDTO updateUser(UserDTO user, Long id);
    void deleteUser(Long id);
}
