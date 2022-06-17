package com.ftn.isa.services;

import com.ftn.isa.DTO.RequestDTO;
import com.ftn.isa.model.Request;
import com.ftn.isa.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    public List<Request> getAllRequests(){return requestRepository.getAllRequests();}

    public void save(Request request){ requestRepository.save(request);}

    public Request findOneById(Long id){
        for (Request request : getAllRequests()){
            if (request.getId().equals(id)){
                return request;
            }
        }
        return null;
    }
}
