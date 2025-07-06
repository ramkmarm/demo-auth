package com.sample_authentication.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample_authentication.dto.SigninRequest;
import com.sample_authentication.dto.SigninResponse;
import com.sample_authentication.model.User;
import com.sample_authentication.security.JwtTokenProvider;
import com.sample_authentication.service.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsersServiceImpl usersService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public SigninResponse login(@RequestBody SigninRequest tokenRequestDTO) throws JsonProcessingException {
        SigninResponse responseDTO = new SigninResponse();
        try {
            authenticate(tokenRequestDTO.getUserName(), tokenRequestDTO.getPassword());
            responseDTO = getSigninResponse(tokenRequestDTO);
        } catch (BadCredentialsException e) {
            responseDTO.setMessage("Invalid username or password");
        }

        if(responseDTO.isValid()) {
            responseDTO.setStatus("success");
            responseDTO.setUserName(tokenRequestDTO.getUserName());
        } else {
            responseDTO.setStatus("failed");
        }
        return responseDTO;
    }

    private void authenticate(String username, String password) throws BadCredentialsException{
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    private String getJwtToken(String userName, String role) {
        String roleName = role != null ? role : "";
        return jwtTokenProvider.createToken(userName, roleName);
    }

    private SigninResponse getSigninResponse(SigninRequest tokenRequestDTO) {
        Optional<User> webUser = usersService.getUserByEmail(tokenRequestDTO.getUserName());
        SigninResponse responseDTO = new SigninResponse(
                getJwtToken(tokenRequestDTO.getUserName(), webUser.get().getName()));
        return responseDTO;
    }

}
