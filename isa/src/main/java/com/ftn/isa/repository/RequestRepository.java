package com.ftn.isa.repository;

import com.ftn.isa.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(nativeQuery = true, value = "select * from request")
    public List<Request> getAllRequests();



}
