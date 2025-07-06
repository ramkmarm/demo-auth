package com.sample_authentication.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SigninResponse {

    private String message;
    @JsonIgnore
    private boolean valid;
    private String token;
    private String status;
    private String userName;

    public SigninResponse(String token) {
        if(token != null && !token.isEmpty()) {
            this.token = token;
            this.valid = Boolean.TRUE;
            this.message = "Signed in successfully";
        } else {
            this.valid = Boolean.FALSE;
            this.message = "Unable to signin";
        }
    }

}
