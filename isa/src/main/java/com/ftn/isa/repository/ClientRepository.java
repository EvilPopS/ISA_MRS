package com.ftn.isa.repository;

import com.ftn.isa.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
}
