package com.sample_authentication.dto;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String name;
    private String username;
    private String email;
    private String contactNumber;
    private String address;
    private String role;

}
