package com.ftn.isa.repository;


import com.ftn.isa.model.CottageOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CottageOwnerRepository extends JpaRepository<CottageOwner, Long> {

    CottageOwner findByEmail(String email);

    //@Query("select c from CottageOwner c where c. = ?1")
    //List<Cottage> findAllCottages(Long id);

    @Query(nativeQuery = true,
            value="SELECT own.* " +
                    "FROM cottage_owner as own " +
                    "JOIN cottage as cot ON cot.cottage_owner_id = own.id " +
                        "WHERE cot.id = :cottage_id"
    )
    CottageOwner getOwnerByCottageId(@Param("cottage_id") double cottageId);
}

