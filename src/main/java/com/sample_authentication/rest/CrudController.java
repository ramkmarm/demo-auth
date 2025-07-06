package com.sample_authentication.rest;

import com.sample_authentication.dto.RolesRequest;
import com.sample_authentication.dto.UserAddRequest;
import com.sample_authentication.dto.UserUpdateRequest;
import com.sample_authentication.model.CommonResponse;
import com.sample_authentication.service.RolesService;
import com.sample_authentication.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/wo")
@Validated
public class CrudController {

    @Autowired
    private UsersService usersService;
    @Autowired
    private RolesService rolesService;

    @PostMapping("/users/create")
    public CommonResponse createUser(@Valid @RequestBody UserAddRequest request) {
        CommonResponse response = usersService.createUser(request);
        return response;
    }

    @PutMapping("/users/update")
    public CommonResponse updateUser(@Valid @RequestBody UserUpdateRequest request,
                                     @RequestParam(value = "id") Long id) {
        CommonResponse response = usersService.updateUser(id, request);
        return response;
    }

    @DeleteMapping("/users/delete")
    public CommonResponse deleteUser(@RequestParam(value = "id") Long id) {
        CommonResponse response = usersService.deleteUser(id);
        return response;
    }

    @PostMapping("/roles/create")
    public CommonResponse createRole(@Valid @RequestBody RolesRequest request) {
        CommonResponse response = rolesService.createRole(request);
        return response;
    }

    @PutMapping("/roles/update")
    public CommonResponse updateRole(@Valid @RequestBody RolesRequest request,
                                     @RequestParam(value = "id") Long id) {
        CommonResponse response = rolesService.updateRole(id, request);
        return response;
    }

    @DeleteMapping("/roles/delete")
    public CommonResponse deleteRole(@RequestParam(value = "id") Long id) {
        CommonResponse response = rolesService.deleteRole(id);
        return response;
    }

}