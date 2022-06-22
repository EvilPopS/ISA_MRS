package com.ftn.isa.controllers;

import com.ftn.isa.model.Client;
import com.ftn.isa.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;


public class TaskAutoScheduler {
    @Autowired
    private ClientService clientService;

    @Scheduled(cron="0 0 0 1 * *")
    public void resetClientPenalties() {
        clientService.resetPenalties();
    }
}
