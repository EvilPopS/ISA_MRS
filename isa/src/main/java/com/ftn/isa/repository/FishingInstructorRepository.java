package com.ftn.isa.repository;

import com.ftn.isa.model.Client;
import com.ftn.isa.model.FishingInstructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FishingInstructorRepository extends JpaRepository<FishingInstructor, Long> {
    FishingInstructor findByEmail(String email);
}
