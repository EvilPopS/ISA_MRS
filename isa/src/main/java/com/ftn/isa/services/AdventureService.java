package com.ftn.isa.services;


import com.ftn.isa.model.Adventure;
import com.ftn.isa.repository.AdventureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdventureService {

    @Autowired
    private AdventureRepository adventureRepository;

    public List<Adventure> searchAdventureByName(String name, Long instructorId){
        return adventureRepository.searchAdventureByName(name, instructorId);
    }


}
