package com.ftn.isa.services;

import com.ftn.isa.DTO.CottageDTO;
import com.ftn.isa.model.Cottage;
import com.ftn.isa.repository.CottageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CottageService {

    @Autowired
    public CottageRepository cottageRepository;

    public Cottage save(Cottage cottage) {
        return cottageRepository.save(cottage);
    }

    public Cottage findById(Long id) {return cottageRepository.getOne(id);}
}
