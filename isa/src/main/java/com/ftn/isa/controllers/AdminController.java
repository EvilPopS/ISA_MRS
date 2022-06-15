package com.ftn.isa.controllers;


import com.ftn.isa.DTO.*;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.model.*;
import com.ftn.isa.security.auth.TokenUtils;
import com.ftn.isa.services.*;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
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

    @Autowired
    CottageOwnerService cottageOwnerService;

    @Autowired
    ClientService clientService;

    @Autowired
    ReportService reportService;

    @Autowired
    EmailService emailService;

    @Autowired
    LoyaltyProgramService loyaltyProgramService;

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
    @GetMapping(value = "/reports")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportDTO>> getAllReports(){
        List<Report> reportList = reportService.getAllReports();
        List<ReportDTO> reportDTOS = new ArrayList<>();
        for (Report rep : reportList){reportDTOS.add(new ReportDTO(rep));}


        return new ResponseEntity<>(reportDTOS, HttpStatus.OK);
    }
    @PostMapping(value="/configure-loyalty-program")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoyaltyProgram>> configureLoyaltyProgram(HttpServletRequest request, @RequestBody  List<LoyaltyProgram> loyaltyPrograms){
        loyaltyProgramService.updateLoyaltyProgram(loyaltyPrograms);


        return new ResponseEntity<>(loyaltyProgramService.getCompleteLoyaltyProgram(), HttpStatus.OK);

    }

    @GetMapping(value="/get-loyalty-program")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoyaltyProgram>> getLoyaltyProgram(){

        return new ResponseEntity<>(loyaltyProgramService.getCompleteLoyaltyProgram(), HttpStatus.OK);
    }

    @PostMapping(value = "/answer-on-report/{reciever}")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> answerOnReport(HttpServletRequest request, @RequestBody AnswerDTO answerDTO, @PathVariable String reciever){
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Report report = reportService.findOneById(answerDTO.getReportId());

        if (reciever.equals("client")){
            emailService.sendMail(answerDTO.getClientEmail(), "Administrator's answer on your report", answerDTO.getMessage());
            report.setAnswered(true);
            reportService.save(report);
            return new ResponseEntity<>(HttpStatus.OK);


        }else{
            report.setAnswered(true);
            reportService.save(report);
            return new ResponseEntity<>(HttpStatus.OK);

        }


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


    @DeleteMapping(value = "/delete-user/{response}")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteUserFromRequest(HttpServletRequest request, @RequestBody RequestDTO requestDTO, @PathVariable String response){
        // za sad samo ovako pa vidi posle kako ces
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        List<Request> requestList = requestService.getAllRequests();

        Request req = requestService.findOneById(requestDTO.getRequestId());

        if (!(fishingInstructorService.findByEmail(req.getSender().getEmail()) == null)){
            FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(req.getSender().getEmail());
            if(response.equals("allow"))
                fishingInstructor.setDeleted(true);
                fishingInstructorService.save(fishingInstructor);
            req.setSender(fishingInstructor);
            req.setAnswered(true);
            requestService.save(req);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        if (!(cottageOwnerService.findByEmail(req.getSender().getEmail()) == null)){
            CottageOwner cottageOwner = cottageOwnerService.findByEmail(req.getSender().getEmail());
            if(response.equals("allow"))
                cottageOwner.setDeleted(true);
            cottageOwnerService.save(cottageOwner);
            req.setSender(cottageOwner);
            req.setAnswered(true);
            requestService.save(req);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        if (!(clientService.findByEmail(req.getSender().getEmail()) == null)){
            Client client = clientService.findByEmail(req.getSender().getEmail());
            if(response.equals("allow"))
                client.setDeleted(true);
            clientService.saveOrUpdateClient(client);
            req.setSender(client);
            req.setAnswered(true);
            requestService.save(req);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/registration/{response}")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> registrationAllowing(HttpServletRequest request, @RequestBody RequestDTO requestDTO, @PathVariable String response){
        // za sad samo ovako pa vidi posle kako ces
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        List<Request> requestList = requestService.getAllRequests();

        Request req = requestService.findOneById(requestDTO.getRequestId());

        if (!(fishingInstructorService.findByEmail(req.getSender().getEmail()) == null)){
            FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(req.getSender().getEmail());
            if(response.equals("allow"))
                fishingInstructor.setActive(true);
            fishingInstructorService.save(fishingInstructor);
            req.setSender(fishingInstructor);
            req.setAnswered(true);
            requestService.save(req);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        if (!(cottageOwnerService.findByEmail(req.getSender().getEmail()) == null)){
            CottageOwner cottageOwner = cottageOwnerService.findByEmail(req.getSender().getEmail());
            if(response.equals("allow"))
                cottageOwner.setActive(true);
            cottageOwnerService.save(cottageOwner);
            req.setSender(cottageOwner);
            req.setAnswered(true);
            requestService.save(req);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        if (!(clientService.findByEmail(req.getSender().getEmail()) == null)){
            Client client = clientService.findByEmail(req.getSender().getEmail());
            if(response.equals("allow"))
                client.setActive(true);
            clientService.saveOrUpdateClient(client);
            req.setSender(client);
            req.setAnswered(true);
            requestService.save(req);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }




}
