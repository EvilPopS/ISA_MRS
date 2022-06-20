package com.ftn.isa.services;

import com.ftn.isa.model.Boat;
import com.ftn.isa.model.Cottage;
import com.ftn.isa.repository.BoatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BoatService {

    @Autowired
    private BoatRepository boatRepository;

    public Boat save(Boat boat) {
        return boatRepository.save(boat);
    }

    public Boat findById(Long id) {return boatRepository.getOne(id);}

}
