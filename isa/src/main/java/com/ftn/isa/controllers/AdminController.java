package com.ftn.isa.controllers;


import com.ftn.isa.DTO.AdminDTO;
import com.ftn.isa.model.Admin;
import com.ftn.isa.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="api/admin")
@CrossOrigin(origins = "http://localhost:8081")
public class AdminController {

    @Autowired
    AdminService adminService;


    @GetMapping(value = "/{email}")
    public ResponseEntity<AdminDTO> getByEmail(@PathVariable String email){
        Admin admin = adminService.findByEmail(email);
        if (admin == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new AdminDTO(admin), HttpStatus.OK);
    }




}
