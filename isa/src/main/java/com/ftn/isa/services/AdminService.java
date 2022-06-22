package com.ftn.isa.services;

import com.ftn.isa.DTO.AdminDTO;
import com.ftn.isa.DTO.AnswerDTO;
import com.ftn.isa.DTO.BoatOwnerDTO;
import com.ftn.isa.model.*;
import com.ftn.isa.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class AdminService  {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    private FishingInstructorService fishingInstructorService;
    @Autowired
    private CottageOwnerService cottageOwnerService;
    @Autowired
    private BoatOwnerService boatOwnerService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private ReportService reportService;

    public Admin findByEmail(String email){
        return adminRepository.findByEmail(email);
    }

    public boolean save(AdminDTO adminDTO, Admin admin) {
        adminDTO.hashPassword();
        admin.updatePersonalInfo(adminDTO);
        adminRepository.save(admin);
        return true;
    }

    @Transactional
    public void answerOnReport(String reciever, AnswerDTO answerDTO) {
        Report report = reportService.findOneById(answerDTO.getReportId());
        report.setChanged(!report.isChanged());
        reportService.save(report);

        if (reciever.equals("client"))
            emailService.sendMail(answerDTO.getClientEmail(), "Administrator's answer on your report", answerDTO.getMessage());
        else
            emailService.sendMail(answerDTO.getOwnerEmail(), "Administrator's answer on your report", answerDTO.getMessage());

        report.setAnswered(true);
        reportService.save(report);
    }


    @Transactional
    public boolean delUserByReq(Request req, String response) {
        req.setChanged(!req.isChanged());
        requestService.save(req);

        if (!(fishingInstructorService.findByEmail(req.getSender().getEmail()) == null)) {
            FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(req.getSender().getEmail());
            if (response.equals("allow"))
                fishingInstructor.setDeleted(true);
            fishingInstructorService.save(fishingInstructor);
            req.setSender(fishingInstructor);
            req.setAnswered(true);
            requestService.save(req);
            return true;
        }

        if (!(boatOwnerService.findByEmail(req.getSender().getEmail()) == null)) {
            BoatOwner boatOwner = boatOwnerService.findByEmail(req.getSender().getEmail());
            if (response.equals("allow"))
                boatOwner.setDeleted(true);
            boatOwnerService.save(boatOwner);
            req.setSender(boatOwner);
            req.setAnswered(true);
            requestService.save(req);
            return true;
        }
        
        if (!(cottageOwnerService.findByEmail(req.getSender().getEmail()) == null)) {
            CottageOwner cottageOwner = cottageOwnerService.findByEmail(req.getSender().getEmail());
            if (response.equals("allow"))
                cottageOwner.setDeleted(true);
            cottageOwnerService.save(cottageOwner);
            req.setSender(cottageOwner);
            req.setAnswered(true);
            requestService.save(req);
            return true;
        }

        if (!(clientService.findByEmail(req.getSender().getEmail()) == null)) {
            Client client = clientService.findByEmail(req.getSender().getEmail());
            if (response.equals("allow"))
                client.setDeleted(true);
            clientService.saveOrUpdateClient(client);
            req.setSender(client);
            req.setAnswered(true);
            requestService.save(req);
            return true;
        }
        
        return false;
    }

    @Transactional
    public boolean registerAllow(Request req, String response) {
        req.setChanged(!req.isChanged());
        requestService.save(req);

        if (!(fishingInstructorService.findByEmail(req.getSender().getEmail()) == null)) {
            FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(req.getSender().getEmail());
            if (response.equals("allow"))
                fishingInstructor.setActive(true);
            fishingInstructorService.save(fishingInstructor);
            req.setSender(fishingInstructor);
            req.setAnswered(true);
            requestService.save(req);
            return true;
        }

        if (!(cottageOwnerService.findByEmail(req.getSender().getEmail()) == null)) {
            CottageOwner cottageOwner = cottageOwnerService.findByEmail(req.getSender().getEmail());
            if (response.equals("allow"))
                cottageOwner.setActive(true);
            cottageOwnerService.save(cottageOwner);
            req.setSender(cottageOwner);
            req.setAnswered(true);
            requestService.save(req);
            return true;
        }

        if (!(boatOwnerService.findByEmail(req.getSender().getEmail()) == null)) {
            BoatOwner boatOwner = boatOwnerService.findByEmail(req.getSender().getEmail());
            if (response.equals("allow"))
                boatOwner.setActive(true);
            boatOwnerService.save(boatOwner);
            req.setSender(boatOwner);
            req.setAnswered(true);
            requestService.save(req);
            return true;
        }

        if (!(clientService.findByEmail(req.getSender().getEmail()) == null)) {
            Client client = clientService.findByEmail(req.getSender().getEmail());
            if (response.equals("allow"))
                client.setActive(true);
            clientService.saveOrUpdateClient(client);
            req.setSender(client);
            req.setAnswered(true);
            requestService.save(req);
            return true;
        }
        return false;
    }

}
