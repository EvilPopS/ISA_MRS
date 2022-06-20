package com.ftn.isa.services;


import com.ftn.isa.model.Adventure;
import com.ftn.isa.model.Cottage;
import com.ftn.isa.repository.AdventureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class AdventureService {

    @Autowired
    private AdventureRepository adventureRepository;

    public List<Adventure> searchAdventureByName(String name, Long instructorId){
        return adventureRepository.searchAdventureByName(name, instructorId);
    }

    public Adventure save(Adventure adventure) {
        return adventureRepository.save(adventure);
    }

    public Adventure findById(Long id) {return adventureRepository.getOne(id);}

}
