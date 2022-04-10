package com.ftn.isa.repository;

import com.ftn.isa.model.Boat;
import com.ftn.isa.model.BoatOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoatOwnerRepository extends JpaRepository<BoatOwner, Long> {

}
