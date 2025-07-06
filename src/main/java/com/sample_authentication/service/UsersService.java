package com.sample_authentication.service;

import com.sample_authentication.dto.RolesResponse;
import com.sample_authentication.dto.UserAddRequest;
import com.sample_authentication.dto.UserResponse;
import com.sample_authentication.dto.UserUpdateRequest;
import com.sample_authentication.model.CommonResponse;

import java.util.List;

public interface UsersService {

    CommonResponse createUser(UserAddRequest request);
    CommonResponse updateUser(Long id, UserUpdateRequest request);
    CommonResponse deleteUser(Long id);
    UserResponse findById(Long id);
    List<UserResponse> findAll();
}
