package com.ftn.isa.controllers;

import com.ftn.isa.DTO.FishingInstructorDTO;
import com.ftn.isa.model.FishingInstructor;
import com.ftn.isa.services.FishingInstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value="api/fishingInstructor")
@CrossOrigin(origins = "http://localhost:8081")
public class FishingInstructorController {
    @Autowired
    private FishingInstructorService fishingInstructorService;


    @GetMapping(value="/{email}")
    public ResponseEntity<FishingInstructorDTO> getByEmail(@PathVariable String email) {
        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(email);
        if (fishingInstructor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new FishingInstructorDTO(fishingInstructor), HttpStatus.OK);
    }


    @PutMapping(consumes="application/json", value="/data-update")
    public ResponseEntity<FishingInstructorDTO> updatePersonalData(@RequestBody FishingInstructorDTO fishingInstructorData) {
        FishingInstructor fishingInstructor = fishingInstructorService.findByEmail(fishingInstructorData.getEmail());

        if (fishingInstructor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (fishingInstructorData.arePropsValid())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        fishingInstructorService.updatePersonalInfo(fishingInstructorData, fishingInstructor);
        return new ResponseEntity<>(fishingInstructorData, HttpStatus.OK);
    }
}