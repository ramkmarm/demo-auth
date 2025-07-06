package com.sample_authentication.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RolesRequest {

    @NotBlank(message = "name is compulsory")
    private String name;

}
