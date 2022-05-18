package com.ftn.isa.controllers;

import com.ftn.isa.DTO.NewReportDTO;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.model.Client;
import com.ftn.isa.model.CottageOwner;
import com.ftn.isa.security.auth.TokenUtils;
import com.ftn.isa.services.ClientService;
import com.ftn.isa.services.CottageOwnerService;
import com.ftn.isa.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "api/notification")
public class NotificationController {

    @Autowired
    private CottageOwnerService cottageOwnerService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private TokenUtils tokenUtils;

    @PostMapping(value = "/add-report")
    @PreAuthorize("hasRole('COTTAGE_OWNER') || hasRole('INSTRUCTOR') || hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> addNewReport(HttpServletRequest request, @RequestBody NewReportDTO reportDTO) {
        String ownerEmail = tokenUtils.getEmailDirectlyFromHeader(request);
        if (ownerEmail == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        CottageOwner cottageOwner = cottageOwnerService.findByEmail(ownerEmail);
        Client client = clientService.findByEmail(reportDTO.getClientEmail());
        if (cottageOwner == null || client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (!reportDTO.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        reportService.addNewReport(cottageOwner, client, reportDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}