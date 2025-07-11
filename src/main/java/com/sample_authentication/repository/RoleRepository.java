package com.sample_authentication.repository;

import com.sample_authentication.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findById(Integer id);
    Optional<Role> findByName(String name);
}
