package com.ftn.isa.controllers;

import com.ftn.isa.DTO.LoyaltyProgramStatsDTO;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.services.LoyaltyProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="api/loyalty-program")
public class LoyaltyController {
    @Autowired
    private LoyaltyProgramService loyaltyProgramService;

    @GetMapping(value="/get-info")
    @PreAuthorize("hasAnyRole('CLIENT', 'COTTAGE_OWNER','BOAT_OWNER','INSTRUCTOR')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<LoyaltyProgramStatsDTO> getLoyaltyProgramInfo() {
        return new ResponseEntity<>(
                new LoyaltyProgramStatsDTO(loyaltyProgramService.getAllLoyaltyPrograms()),
                HttpStatus.OK
        );
    }
}
