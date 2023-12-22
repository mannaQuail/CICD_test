package com.hacker.hacker.service;

import com.hacker.hacker.common.ApiResponse;
import com.hacker.hacker.common.response.SuccessMessage;
import com.hacker.hacker.dto.UserDTO;
import com.hacker.hacker.dto.UserListDTO;
import com.hacker.hacker.repository.UserRepository;
import com.hacker.hacker.model.Users;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ApiResponse<UserDTO> getUsersById(int userId){
        UserDTO userDTO = new UserDTO();
        Users user = userRepository.findByUserId(userId);
        userDTO.setUserId(user.getUserId());
        userDTO.setName(user.getName());
        userDTO.setProfileImageUrl(user.getProfileImageUrl());

        return ApiResponse.success(SuccessMessage.GET_USERS_BY_ID_SUCCESS, userDTO);
    }

    public ApiResponse<List<UserListDTO>> getAllUsers(){
        List<Users> usersList = userRepository.findAll();
        List<UserListDTO> userListDTOS = new ArrayList<>();
        for(Users user : usersList){
            UserListDTO userListDTO = new UserListDTO();
            userListDTO.setUserId(user.getUserId());
            userListDTO.setName(user.getName());
            userListDTOS.add(userListDTO);
        }
        return ApiResponse.success(SuccessMessage.GET_ALL_USERS_SUCCESS,userListDTOS);
    }
}