package com.ftn.isa.services;

import com.ftn.isa.DTO.CottageDTO;
import com.ftn.isa.model.Cottage;
import com.ftn.isa.repository.CottageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CottageService {

    @Autowired
    public CottageRepository cottageRepository;

    public Cottage save(Cottage cottage) {
        return cottageRepository.save(cottage);
    }
}
