package com.ftn.isa.repository;


import com.ftn.isa.model.CottageOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CottageOwnerRepository extends JpaRepository<CottageOwner, Long> {

    CottageOwner findByEmail(String email);
}

