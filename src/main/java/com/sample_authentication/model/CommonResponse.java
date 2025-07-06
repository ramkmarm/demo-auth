package com.sample_authentication.model;

import lombok.Data;

@Data
public class CommonResponse {

    private String message;
    private boolean valid;
    private Object data;

}
