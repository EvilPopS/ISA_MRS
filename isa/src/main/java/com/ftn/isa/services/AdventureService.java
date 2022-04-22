package com.ftn.isa.services;


import com.ftn.isa.repository.AdventureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdventureService {

    @Autowired
    private AdventureRepository adventureRepository;


}
