package com.ftn.isa.services;

import com.ftn.isa.model.User;
import com.ftn.isa.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    FishingInstructorRepository fishingInstructorRepository;
    @Autowired
    CottageOwnerRepository cottageOwnerRepository;
    @Autowired
    BoatOwnerRepository boatOwnerRepository;

    private User findByEmail(String email) {
        User usr;
        if ((usr = clientRepository.findByEmail(email)) != null)
            return usr;

        if ((usr = cottageOwnerRepository.findByEmail(email)) != null)
            return usr;

        if ((usr = boatOwnerRepository.findByEmail(email)) != null)
            return usr;

        if ((usr = fishingInstructorRepository.findByEmail(email)) != null)
            return usr;

        usr = adminRepository.findByEmail(email);
        return usr;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("Not found!");
        return (UserDetails) user;
    }
}
