package com.ftn.isa.controllers;

import com.ftn.isa.DTO.LoginDTO;
import com.ftn.isa.model.User;
import com.ftn.isa.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="api/user")
public class UserController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private CottageOwnerService cottageOwnerService;
    @Autowired
    private BoatOwnerService boatOwnerService;
    @Autowired
    private FishingInstructorService instructorService;
    @Autowired
    private AdminService adminService;

    @PostMapping(value="/login")
    @CrossOrigin(origins = "http://localhost:8081")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginData) {
        if (checkIfLoginSuccess(clientService.findByEmail(loginData.getEmail()), loginData))
            return new ResponseEntity<>("client", HttpStatus.OK);

        if (checkIfLoginSuccess(cottageOwnerService.findByEmail(loginData.getEmail()), loginData))
            return new ResponseEntity<>("cottageOwner", HttpStatus.OK);

        if (checkIfLoginSuccess(boatOwnerService.findByEmail(loginData.getEmail()), loginData))
            return new ResponseEntity<>("boatOwner", HttpStatus.OK);

        if (checkIfLoginSuccess(instructorService.findByEmail(loginData.getEmail()), loginData))
            return new ResponseEntity<>("instructor", HttpStatus.OK);

        if (checkIfLoginSuccess(adminService.findByEmail(loginData.getEmail()), loginData))
            return new ResponseEntity<>("admin", HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private boolean checkIfLoginSuccess(User usr, LoginDTO loginData) {
        return usr != null && usr.isActive() && usr.getPassword().equals(loginData.getPassword());
    }
}
