package com.sample_authentication.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserAddRequest {

    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "mobile is required")
    private String mobile;
    @NotBlank(message = "email is required")
    private String email;
    private String address;
    @NotBlank(message = "password is required")
    private String password;
    private long roleId;
}
