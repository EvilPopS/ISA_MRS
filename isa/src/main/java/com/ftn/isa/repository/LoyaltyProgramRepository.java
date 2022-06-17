package com.ftn.isa.repository;

import com.ftn.isa.model.LoyaltyProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoyaltyProgramRepository extends JpaRepository<LoyaltyProgram, Long> {
    @Query(nativeQuery = true, value="select * from loyalty_program")
    public List<LoyaltyProgram> getCompleteLoyaltyProgram();

    @Query(nativeQuery = true, value = "select * from loyalty_program")
    public List<LoyaltyProgram> getAllLoyaltyPrograms();
}
