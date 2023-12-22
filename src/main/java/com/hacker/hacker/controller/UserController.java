package com.hacker.hacker.controller;

import com.hacker.hacker.common.ApiResponse;
import com.hacker.hacker.service.UserService;
import com.hacker.hacker.dto.UserListDTO;
import com.hacker.hacker.dto.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ApiResponse<List<UserListDTO>> getAllUsers() { return userService.getAllUsers(); }

    @GetMapping("/users/{userId}")
    public ApiResponse<UserDTO> getUsersById(@PathVariable int userId) { return userService.getUsersById(userId); }
}