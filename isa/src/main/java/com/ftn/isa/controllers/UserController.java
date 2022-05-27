package com.ftn.isa.controllers;

import com.ftn.isa.DTO.DelReqDTO;
import com.ftn.isa.model.Request;
import com.ftn.isa.security.auth.TokenUtils;
import com.ftn.isa.services.RequestService;
import com.ftn.isa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private TokenUtils tokenUtils;

    @PostMapping(value="/{usrType}/sendDeleteRequest")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN','COTTAGE_OWNER','BOAT_OWNER','INSTRUCTOR')")
    public ResponseEntity<HttpStatus> sendDeletionRequest(@PathVariable String usrType, @RequestBody DelReqDTO delReq, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        delReq.setSender(userService.getUserByEmailAndRole(email, usrType));
        requestService.save(new Request(delReq));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
