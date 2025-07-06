package com.sample_authentication.service;

import com.sample_authentication.dto.RolesResponse;
import com.sample_authentication.dto.UserAddRequest;
import com.sample_authentication.dto.UserResponse;
import com.sample_authentication.dto.UserUpdateRequest;
import com.sample_authentication.model.CommonResponse;
import com.sample_authentication.model.Role;
import com.sample_authentication.model.User;
import com.sample_authentication.repository.RoleRepository;
import com.sample_authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    public CommonResponse createUser(UserAddRequest request) {
        CommonResponse commonResponse = new CommonResponse();
        Optional<Role> role = roleRepository.findById(request.getRoleId());
        Optional<User> existingUserByEmail = userRepository.findByEmailAndDeletedFalse(request.getEmail());
        if(role.isEmpty()) {
            commonResponse.setMessage("Invalid role");
            return commonResponse;
        }
        if(existingUserByEmail.isPresent()) {
            commonResponse.setMessage("User already exists for email");
            return commonResponse;
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getEmail());
        user.setAddress(request.getAddress());
        user.setRole(role.get());
        user.setContactNumber(request.getMobile());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User addedUser = userRepository.save(user);
        if(addedUser != null) {
            commonResponse.setValid(Boolean.TRUE);
            commonResponse.setMessage("User created successfully");
            commonResponse.setData("Id is " + user.getId());
        } else {
            commonResponse.setMessage("Error while creating user");
        }
        return commonResponse;
    }

    public CommonResponse updateUser(Long id, UserUpdateRequest request) {
        CommonResponse commonResponse = new CommonResponse();

        Optional<User> optionalUser = userRepository.findByIdAndDeletedFalse(id);
        Optional<Role> optionalRole = roleRepository.findById(request.getRoleId());
        if(optionalUser.isEmpty()) {
            commonResponse.setMessage("Invalid user");
            return commonResponse;
        }
        if(optionalRole.isEmpty()) {
            commonResponse.setMessage("Invalid role");
            return commonResponse;
        }
        User user = optionalUser.get();
        user.setId(id);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setRole(optionalRole.get());
        user.setContactNumber(request.getMobile());
        User updatedUser = userRepository.save(user);
        if(updatedUser != null) {
            commonResponse.setValid(Boolean.TRUE);
            commonResponse.setMessage("User updated successfully");
        } else {
            commonResponse.setMessage("Error while creating user");
        }
        return commonResponse;
    }

    public CommonResponse deleteUser(Long id) {
        CommonResponse commonResponse = new CommonResponse();
        Optional<User> optionalUser = userRepository.findByIdAndDeletedFalse(id);
        if(optionalUser.isEmpty()) {
            commonResponse.setMessage("Invalid user");
            return commonResponse;
        }
        User user = optionalUser.get();
        user.setDeleted(Boolean.TRUE);
        User updatedUser = userRepository.save(user);
        if(updatedUser != null && updatedUser.isDeleted()) {
            commonResponse.setValid(Boolean.TRUE);
            commonResponse.setMessage("User deleted successfully");
        } else {
            commonResponse.setMessage("Error while creating user");
        }
        return commonResponse;
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailAndDeletedFalse(email);
    }

    @Override
    public UserResponse findById(Long id) {
        Optional<User> optionalUser = userRepository.findByIdAndDeletedFalse(id);
        if(optionalUser.isEmpty()) {
            return null;
        } else {
            User user = optionalUser.get();
            UserResponse usersResponse = new UserResponse();
            usersResponse.setId(user.getId());
            usersResponse.setName(user.getName());
            usersResponse.setContactNumber(user.getContactNumber());
            usersResponse.setUsername(user.getUsername());
            usersResponse.setEmail(user.getEmail());
            usersResponse.setRole(user.getRole());
            return usersResponse;
        }
    }

    @Override
    public List<UserResponse> findAll() {
        List<User> userList = userRepository.findAllByDeletedFalse();
        if(userList != null) {
            return userList.stream()
                    .map(user -> {
                        UserResponse usersResponse = new UserResponse();
                        usersResponse.setId(user.getId());
                        usersResponse.setName(user.getName());
                        usersResponse.setContactNumber(user.getContactNumber());
                        usersResponse.setUsername(user.getUsername());
                        usersResponse.setEmail(user.getEmail());
                        usersResponse.setRole(user.getRole());
                        return usersResponse;
                    }).toList();
        }
        return Collections.emptyList();
    }

}
