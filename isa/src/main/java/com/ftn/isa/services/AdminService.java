package com.ftn.isa.services;

import com.ftn.isa.model.Admin;
import com.ftn.isa.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminService  {

    @Autowired
    AdminRepository adminRepository;

    public Admin findByEmail(String email){
        return adminRepository.findByEmail(email);
    }
}
