package com.sample_authentication.service;

import com.sample_authentication.dto.RolesRequest;
import com.sample_authentication.dto.RolesResponse;
import com.sample_authentication.model.CommonResponse;

import java.util.List;

public interface RolesService {

    CommonResponse createRole(RolesRequest request);
    CommonResponse updateRole(Long id, RolesRequest request);
    CommonResponse deleteRole(Long id);
    RolesResponse findById(Long id);
    List<RolesResponse> findAll();
}
