package com.ftn.isa.controllers;

import com.ftn.isa.DTO.ClientReportDTO;
import com.ftn.isa.DTO.NewReportDTO;
import com.ftn.isa.DTO.RentalReviewDTO;
import com.ftn.isa.DTO.ReviewOwnerDTO;
import com.ftn.isa.configs.ServerConfig;
import com.ftn.isa.model.Client;
import com.ftn.isa.model.CottageOwner;
import com.ftn.isa.model.FishingInstructor;
import com.ftn.isa.model.User;
import com.ftn.isa.security.auth.TokenUtils;
import com.ftn.isa.services.ClientService;
import com.ftn.isa.services.CottageOwnerService;
import com.ftn.isa.services.FishingInstructorService;
import com.ftn.isa.services.ReportService;
import com.ftn.isa.services.UserService;
import com.ftn.isa.model.*;
import com.ftn.isa.security.auth.TokenUtils;
import com.ftn.isa.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/notification")
public class NotificationController {
    @Autowired
    private CottageOwnerService cottageOwnerService;
    @Autowired
    private BoatOwnerService boatOwnerService;
    @Autowired
    private FishingInstructorService instructorService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private RentalServService rentalServService;


    @Autowired
    private UserService userService;

    @Autowired
    FishingInstructorService fishingInstructorService;

    @PostMapping(value = "/add-report")
    @PreAuthorize("hasRole('COTTAGE_OWNER') || hasRole('INSTRUCTOR') || hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> addNewReport(HttpServletRequest request, @RequestBody NewReportDTO reportDTO) {
        String ownerEmail = tokenUtils.getEmailDirectlyFromHeader(request);
        if (ownerEmail == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        User owner = userService.getUserByEmailAndRole(ownerEmail, reportDTO.getOwnerRole());
        Client client = clientService.findByEmail(reportDTO.getClientEmail());
        
        if (!reportDTO.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        if (owner == null || client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
        reportService.addNewReport(owner, client, reportDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @GetMapping(value = "/get-rental-reviews/{rentalId}")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<List<RentalReviewDTO>> addNewReport(@PathVariable Long rentalId) {
        List<RentalReviewDTO> revs = new ArrayList<>();
        for(Review rev : reviewService.getReviewsForRental(rentalId))
            revs.add(new RentalReviewDTO(rev));

        return new ResponseEntity<>(revs, HttpStatus.OK);
    }

    @PostMapping(value = "/new-review/{rentalId}")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> sendNewReview(@PathVariable Long rentalId, @RequestBody RentalReviewDTO reviewDTO,
                                                    @RequestParam String rentalType, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Client client = clientService.findByEmail(email);
        for (Reservation res : client.getReservations())
            if (!res.isCanceled() && res.getRental().getId().equals(rentalId)
                    && res.getEndTime().isBefore(LocalDateTime.now())) {
                reviewService.makeNewReview(
                        reviewDTO,
                        client,
                        rentalServService.getEntityByTypeAndId(rentalType, rentalId)
                );
                return new ResponseEntity<>(HttpStatus.OK);
            }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/get-reviews/{rentalId}")
    @PreAuthorize("hasRole('COTTAGE_OWNER') || hasRole('INSTRUCTOR') || hasRole('BOAT_OWNER')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<List<ReviewOwnerDTO>> getReviewsForRental(@PathVariable Long rentalId, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<ReviewOwnerDTO> revs = new ArrayList<>();
        for(Review rev : reviewService.getReviewsForRental(rentalId))
            revs.add(new ReviewOwnerDTO(rev));

        return new ResponseEntity<>(revs, HttpStatus.OK);

    }

    @PostMapping(value = "/new-report/{rentalId}")
    @PreAuthorize("hasRole('CLIENT')")
    @CrossOrigin(origins = ServerConfig.FRONTEND_ORIGIN)
    public ResponseEntity<HttpStatus> sendNewReport(@PathVariable Long rentalId, @RequestBody ClientReportDTO reportDTO,
                                                    @RequestParam String rentalType, HttpServletRequest request) {
        String email = tokenUtils.getEmailDirectlyFromHeader(request);
        if (email == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        User owner;
        switch (rentalType) {
            case "Cottage":
                owner = cottageOwnerService.getOwnerByCottageId(rentalId);
                break;
            case "Adventure":
                owner = instructorService.getOwnerByAdventureId(rentalId);
                break;
            default:
                owner = boatOwnerService.getOwnerByBoatId(rentalId);
        }

        Client client = clientService.findByEmail(email);
        reportService.makeNewReport(
                reportDTO.getReport(),
                client,
                owner
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
