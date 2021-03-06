package com.ftn.isa.services;

import com.ftn.isa.model.Address;
import com.ftn.isa.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    public AddressRepository addressRepository;

    public Address save(String city, String country, String street) {
        return addressRepository.save(new Address(country, city,  street));
    }
}
