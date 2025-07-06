package com.sample_authentication.service;

import com.sample_authentication.dto.RolesRequest;
import com.sample_authentication.dto.RolesResponse;
import com.sample_authentication.model.CommonResponse;
import com.sample_authentication.model.Role;
import com.sample_authentication.model.User;
import com.sample_authentication.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RolesServiceImpl implements RolesService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public CommonResponse createRole(RolesRequest request) {
        CommonResponse commonResponse = new CommonResponse();
        Optional<Role> existingRole = roleRepository.findByName(request.getName());
        if(existingRole.isPresent()) {
            commonResponse.setMessage("Role already present");
            return commonResponse;
        }

        Role role = new Role();
        role.setName(request.getName());
        Role addedRole = roleRepository.save(role);
        if(addedRole != null && addedRole.getId() > 0) {
            commonResponse.setValid(Boolean.TRUE);
            commonResponse.setMessage("Role created successfully");
            commonResponse.setData(addedRole);
        } else {
            commonResponse.setMessage("Error while creating role");
        }
        return commonResponse;
    }

    @Override
    public CommonResponse updateRole(Long id, RolesRequest request) {
        CommonResponse commonResponse = new CommonResponse();
        Optional<Role> existingRole = roleRepository.findById(id);
        if(existingRole.isEmpty()) {
            commonResponse.setMessage("Role not exists");
            return commonResponse;
        }
        Optional<Role> existingRoleByName = roleRepository.findByName(request.getName());
        if(existingRoleByName.isPresent()) {
            commonResponse.setMessage("Role name already exists");
            return commonResponse;
        }

        existingRole.get().setName(request.getName());
        Role udpatedRole = roleRepository.save(existingRole.get());
        if(udpatedRole != null && udpatedRole.getId() > 0) {
            commonResponse.setValid(Boolean.TRUE);
            commonResponse.setMessage("Role updated successfully");
            commonResponse.setData(udpatedRole);
        } else {
            commonResponse.setMessage("Error while updating role");
        }
        return commonResponse;
    }

    @Override
    public CommonResponse deleteRole(Long id) {
        CommonResponse commonResponse = new CommonResponse();
        Optional<Role> optionalRole = roleRepository.findById(id);
        if(optionalRole.isEmpty()) {
            commonResponse.setMessage("Invalid role");
            return commonResponse;
        }
        roleRepository.delete(optionalRole.get());
        commonResponse.setValid(Boolean.TRUE);
        commonResponse.setMessage("Role deleted successfully");
        return commonResponse;
    }

    @Override
    public RolesResponse findById(Long id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if(optionalRole.isEmpty()) {
            return null;
        } else {
            Role role = optionalRole.get();
            RolesResponse rolesResponse = new RolesResponse();
            rolesResponse.setId(role.getId());
            rolesResponse.setName(role.getName());
            return rolesResponse;
        }
    }

    @Override
    public List<RolesResponse> findAll() {
        List<Role> rolesList = roleRepository.findAll();
        if(rolesList != null) {
            return rolesList.stream()
                    .map(role -> new RolesResponse(role.getId(), role.getName())).toList();
        }
        return Collections.emptyList();
    }

}
