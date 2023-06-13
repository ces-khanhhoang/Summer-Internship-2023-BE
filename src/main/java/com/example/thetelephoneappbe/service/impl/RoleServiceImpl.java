package com.example.thetelephoneappbe.service.impl;

import com.example.thetelephoneappbe.model.Role;
import com.example.thetelephoneappbe.repository.RoleRepository;
import com.example.thetelephoneappbe.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;

    @Autowired
    public void setStoreRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }
}

