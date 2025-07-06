package com.sample_authentication.dto;

import lombok.Data;

@Data
public class SigninRequest {
    private String userName;
    private String password;
}
