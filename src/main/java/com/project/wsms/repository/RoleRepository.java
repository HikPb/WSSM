package com.project.wsms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.wsms.model.ERole;
import com.project.wsms.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
   Optional<Role> findByName(ERole name);
}
