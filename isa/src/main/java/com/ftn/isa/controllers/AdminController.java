package com.ftn.isa.controllers;


import com.ftn.isa.DTO.AdminDTO;
import com.ftn.isa.DTO.RequestDTO;
import com.ftn.isa.model.Admin;
import com.ftn.isa.model.FishingInstructor;
import com.ftn.isa.model.Request;
import com.ftn.isa.model.Response;
import com.ftn.isa.services.AdminService;
import com.ftn.isa.services.FishingInstructorService;
import com.ftn.isa.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="api/admin")
@CrossOrigin(origins = "http://localhost:8081")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    RequestService requestService;

    @Autowired
    FishingInstructorService fishingInstructorService;


    @GetMapping(value = "/{email}")
    public ResponseEntity<AdminDTO> getByEmail(@PathVariable String email){
        Admin admin = adminService.findByEmail(email);
        if (admin == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new AdminDTO(admin), HttpStatus.OK);
    }

    @GetMapping(value = "/requests")
    public ResponseEntity<List<RequestDTO>> getAllRequests(){
        List<Request> requestList = requestService.getAllRequests();
        List<RequestDTO> requestDTOS = new ArrayList<>();
        for (Request req : requestList){ requestDTOS.add(new RequestDTO(req));}

        return new ResponseEntity<>(requestDTOS, HttpStatus.OK);

    }

    @DeleteMapping(value = "/deleteUser/{userId}/{response}")
    public ResponseEntity<HttpStatus> deleteUserFromReques(@PathVariable String userId, @PathVariable String response){
        // za sad samo ovako pa vidi posle kako ces
        List<Request> requestList = requestService.getAllRequests();
        String userEmail="";
        for (Request req : requestList){
            if (req.getSender().getId() == Long.parseLong(userId)){
                FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(req.getSender().getEmail());
                if (response.equals("allow")){
                    fishingInstructor.setDeleted(true);
                    fishingInstructorService.save(fishingInstructor);
                }
                req.setAnswered(true);
                requestService.save(req);

                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }




}
