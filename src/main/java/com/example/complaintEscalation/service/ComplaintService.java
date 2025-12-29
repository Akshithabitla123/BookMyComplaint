package com.example.complaintEscalation.service;

import com.example.complaintEscalation.enums.ComplaintStatus;
import com.example.complaintEscalation.model.Complaint;
import com.example.complaintEscalation.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepo;

    //save complaint
    public String save(Complaint complaint){
        complaintRepo.save(complaint);
        return "Success";
    }

    //get all complaints
    public List<Complaint> getAllComplaints(){
        return complaintRepo.findAll();
    }

    //get complaint by id
    public Complaint getById(int id){
        return complaintRepo.findById(id).orElse(new Complaint());
    }

    //get complaint by title and description(containing)
    public List<Complaint> getByTitleOrDescription(String title,String description){
        return complaintRepo.findByTitleOrDescriptionContaining(title,description);
    }

    //get list of complaints by its status
    public List<Complaint> getByStatus(ComplaintStatus status){
        return complaintRepo.findByStatus(status);
    }

}
