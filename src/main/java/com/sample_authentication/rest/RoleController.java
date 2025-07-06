package com.sample_authentication.rest;

import com.sample_authentication.dto.RolesRequest;
import com.sample_authentication.dto.RolesResponse;
import com.sample_authentication.dto.UserAddRequest;
import com.sample_authentication.dto.UserUpdateRequest;
import com.sample_authentication.model.CommonResponse;
import com.sample_authentication.repository.RoleRepository;
import com.sample_authentication.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RolesService rolesService;

    @GetMapping("/find/{id}")
    public CommonResponse findRole(@PathVariable(value = "id") Long id) {
        RolesResponse response = rolesService.findById(id);
        CommonResponse finalResponse = new CommonResponse();
        if(response == null) {
            finalResponse.setMessage("Role not found");
        } else {
            finalResponse.setValid(Boolean.TRUE);
            finalResponse.setMessage("Role loaded successfully");
            finalResponse.setData(response);
        }
        return finalResponse;
    }

    @GetMapping("/list")
    public CommonResponse listRoles() {
        List<RolesResponse> response = rolesService.findAll();
        CommonResponse finalResponse = new CommonResponse();
        if(response == null || response.isEmpty()) {
            finalResponse.setMessage("Roles not found");
        } else {
            finalResponse.setValid(Boolean.TRUE);
            finalResponse.setMessage("Roles loaded successfully");
            finalResponse.setData(response);
        }
        return finalResponse;
    }

}
