package com.ftn.isa.repository;

import com.ftn.isa.model.FishingInstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FishingInstructorRepository extends JpaRepository<FishingInstructor, Long> {
    FishingInstructor findByEmail(String email);

    FishingInstructor getOne(Long id);

    @Query(nativeQuery = true,
            value="SELECT inst.* " +
                    "FROM fishing_instructor as inst " +
                    "JOIN adventure as adv ON adv.instructor_id = inst.id " +
                    "WHERE adv.id = :adventure_id"
    )
    FishingInstructor getOwnerByAdventureId(@Param("adventure_id") double adventureId);

}
