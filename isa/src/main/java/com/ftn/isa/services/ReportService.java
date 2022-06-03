package com.ftn.isa.services;

import com.ftn.isa.DTO.NewReportDTO;
import com.ftn.isa.helpers.Validate;
import com.ftn.isa.model.Client;
import com.ftn.isa.model.Report;
import com.ftn.isa.model.User;
import com.ftn.isa.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public void save(Report report){ reportRepository.save(report);}

    public void addNewReport(User owner, Client client, NewReportDTO reportDTO) {
        Report report = new Report(reportDTO.getMessage(), false, Validate.sdf.format(new Date()),
                client, owner, reportDTO.isHasShowedUp(), reportDTO.isNegative());

        this.save(report);
    }

    public void makeNewReport(String message, Client client, User owner) {
        reportRepository.save(new Report(message, client, owner, true));
    }

}
