package com.ftn.isa.controllers;

import com.ftn.isa.DTO.LoginDTO;
import com.ftn.isa.DTO.ReservationHistoryDTO;
import com.ftn.isa.DTO.UserRegDTO;
import com.ftn.isa.DTO.UserTokenDTO;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.model.Client;
import com.ftn.isa.model.User;
import com.ftn.isa.security.auth.TokenUtils;
import com.ftn.isa.services.ClientService;
import com.ftn.isa.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="api/unauth")
public class UnauthUserController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenUtils tokenUtils;

    @PostMapping(consumes="application/json", value="/register/client")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> registerClient(@RequestBody UserRegDTO clientData) {
        if (!clientData.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (clientService.findByEmail(clientData.getEmail()) != null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Client client = new Client(clientData);
        try {
            clientService.saveOrUpdateClient(client);
        } catch (Exception ignored) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            emailService.sendMail(client, "Confirmation mail",
                    "Click here to activate your account: http://localhost:8080/api/unauth/confirm-mail/" +
                            client.getEmail()
            );
        } catch(Exception ignored){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="/confirm-mail/{email}")
    public ResponseEntity<HttpStatus> activateClientAccount(@PathVariable String email){
        Client client = clientService.findByEmail(email);
        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        client.setActive(true);
        clientService.saveOrUpdateClient(client);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value="/login")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<UserTokenDTO> login(@RequestBody LoginDTO loginData) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginData.getEmail(), loginData.getPassword()
                    )
            );
        } catch (Exception ignored) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        if(!user.isActive() || user.isDeleted())
            return new ResponseEntity<>(null, HttpStatus.LOCKED);

        return new ResponseEntity<>(
                new UserTokenDTO(
                    tokenUtils.generateToken(user.getEmail(), user.getRole().getName().substring(5)),
                    tokenUtils.getExpiredIn()
                ),
                HttpStatus.OK
        );
    }

}
