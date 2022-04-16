package com.ftn.isa.repository;


import com.ftn.isa.model.Cottage;
import com.ftn.isa.model.CottageOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CottageOwnerRepository extends JpaRepository<CottageOwner, Long> {

    CottageOwner findByEmail(String email);

    //@Query("select c from CottageOwner c where c. = ?1")
    //List<Cottage> findAllCottages(Long id);
}

