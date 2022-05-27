package com.ftn.isa.controllers;


import com.ftn.isa.DTO.AdminDTO;
import com.ftn.isa.DTO.RequestDTO;
import com.ftn.isa.DTO.ReviewDTO;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.model.*;
import com.ftn.isa.security.auth.TokenUtils;
import com.ftn.isa.services.AdminService;
import com.ftn.isa.services.FishingInstructorService;
import com.ftn.isa.services.RequestService;
import com.ftn.isa.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    ReviewService reviewService;

    @Autowired
    TokenUtils tokenUtils;

    @PostMapping(value="/sendDeleteRequest/{email}/{userType}")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RequestDTO> addRequest(@RequestBody RequestDTO requestDTO, @PathVariable String email, @PathVariable String userType){
        User user = null;
        switch (userType){
            case "INSTRUCTOR":
                user = fishingInstructorService.findByEmail(email);
        }

        Request request = new Request(requestDTO.getMessage(), requestDTO.isAnswered(), requestDTO.getSentTime(), requestDTO.getRequestType(), user);

        requestService.save(request);

        return new ResponseEntity<>(HttpStatus.OK);



    }

    @GetMapping
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDTO> getByEmail(HttpServletRequest request){
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Admin admin = adminService.findByEmail(email);
        if (admin == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new AdminDTO(admin), HttpStatus.OK);
    }

    @GetMapping(value = "/requests")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RequestDTO>> getAllRequests(){
        List<Request> requestList = requestService.getAllRequests();
        List<RequestDTO> requestDTOS = new ArrayList<>();
        for (Request req : requestList){ requestDTOS.add(new RequestDTO(req));}

        return new ResponseEntity<>(requestDTOS, HttpStatus.OK);

    }

    @GetMapping(value = "/reviews")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReviewDTO>> getAllReviews(){
        List<Review> reviewList = reviewService.getAllReviews();
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        for (Review review : reviewList){reviewDTOS.add(new ReviewDTO(review));}

        return new ResponseEntity<>(reviewDTOS, HttpStatus.OK);
    }


    @DeleteMapping(value = "/deleteUser/{userId}/{response}")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PreAuthorize("hasRole('ADMIN')")
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
                req.setSender(fishingInstructor);
                requestService.save(req);

                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }




}
