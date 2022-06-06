package com.ftn.isa.controllers;

import com.ftn.isa.DTO.LoyaltyProgramStatsDTO;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.security.auth.TokenUtils;
import com.ftn.isa.services.ClientService;
import com.ftn.isa.services.LoyaltyProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/loyalty-program")
public class LoyaltyController {
    @Autowired
    private LoyaltyProgramService loyaltyProgramService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private ClientService clientService;

    @GetMapping(value="/get-info")
    @PreAuthorize("hasAnyRole('CLIENT', 'COTTAGE_OWNER','BOAT_OWNER','INSTRUCTOR')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<LoyaltyProgramStatsDTO> getLoyaltyProgramInfo() {
        return new ResponseEntity<>(
                new LoyaltyProgramStatsDTO(loyaltyProgramService.getAllLoyaltyPrograms()),
                HttpStatus.OK
        );
    }

    @GetMapping(value="/get-client-discount")
    @PreAuthorize("hasAnyRole('CLIENT', 'COTTAGE_OWNER','BOAT_OWNER','INSTRUCTOR')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<Double> getProperClientDiscount(HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(loyaltyProgramService.getClientDiscount(clientService.findByEmail(email)), HttpStatus.OK);
    }

}
