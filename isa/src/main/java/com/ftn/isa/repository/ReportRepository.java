package com.ftn.isa.repository;

import com.ftn.isa.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query(nativeQuery = true, value = "select * from report")
    public List<Report> getAllReports();

}
