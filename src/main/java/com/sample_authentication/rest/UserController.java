package com.sample_authentication.rest;

import com.sample_authentication.dto.RolesResponse;
import com.sample_authentication.dto.UserResponse;
import com.sample_authentication.model.CommonResponse;
import com.sample_authentication.model.User;
import com.sample_authentication.service.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UsersServiceImpl userService;

    @GetMapping("/find/{id}")
    public CommonResponse findUser(@PathVariable(value = "id") Long id) {
        UserResponse response = userService.findById(id);
        CommonResponse finalResponse = new CommonResponse();
        if(response == null) {
            finalResponse.setMessage("User not found");
        } else {
            finalResponse.setValid(Boolean.TRUE);
            finalResponse.setMessage("User loaded successfully");
            finalResponse.setData(response);
        }
        return finalResponse;
    }

    @GetMapping("/list")
    public CommonResponse listUsers() {
        List<UserResponse> response = userService.findAll();
        CommonResponse finalResponse = new CommonResponse();
        if(response == null || response.isEmpty()) {
            finalResponse.setMessage("Users not found");
        } else {
            finalResponse.setValid(Boolean.TRUE);
            finalResponse.setMessage("Users loaded successfully");
            finalResponse.setData(response);
        }
        return finalResponse;
    }
}
