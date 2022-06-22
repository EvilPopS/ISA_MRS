package com.ftn.isa.services;

import com.ftn.isa.DTO.AdminDTO;
import com.ftn.isa.DTO.BoatOwnerDTO;
import com.ftn.isa.model.Admin;
import com.ftn.isa.model.BoatOwner;
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

    public boolean save(AdminDTO adminDTO, Admin admin) {
        adminDTO.hashPassword();
        admin.updatePersonalInfo(adminDTO);
        adminRepository.save(admin);
        return true;
    }
}
