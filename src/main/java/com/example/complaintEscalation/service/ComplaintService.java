package com.example.complaintEscalation.service;

import com.example.complaintEscalation.dto.DetailsDto;
import com.example.complaintEscalation.enums.ComplaintStatus;
import com.example.complaintEscalation.model.Complaint;
import com.example.complaintEscalation.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

    //get all complaints by id
    public List<Complaint> getById(int id){
        return complaintRepo.findByUser_userId(id);
    }

    //get complaint by title
    public List<Complaint> findByTitle(String title){
        return complaintRepo.findByTitleContaining(title);
    }



    //update status of complaint
    public Complaint updateStatus(int complaintId,ComplaintStatus status){
        Complaint complaint=complaintRepo.findById(complaintId).orElseThrow(()->new RuntimeException("Complaint not found"));
        complaint.setStatus(status);
        complaint.setUpdatedAt(LocalDate.now());
        return complaintRepo.save(complaint);
    }

    //assign staff to the complaint
    public Complaint assignStaff(int complaintId, String staffName){
        Complaint complaint=complaintRepo.findById(complaintId).orElseThrow(()->new RuntimeException("Complaint not found"));
        complaint.setAssignedStaff(staffName);
        complaint.setUpdatedAt(LocalDate.now());
        return complaintRepo.save(complaint);
    }

    //filter by area,status, date range
    public List<Complaint> findByArea(String area){
        return complaintRepo.findByAreaIgnoreCase(area);
    }
    public List<Complaint> findByStatus(ComplaintStatus status){
        return complaintRepo.findByStatus(status);
    }
    public List<Complaint> findByDate(LocalDateTime from, LocalDateTime to){
        return complaintRepo.findByCreatedAtBetween(from, to);
    }

    //get details of the complaint (dto)
    public DetailsDto getDetails(int id){
        return complaintRepo.findDetails(id);
    }
}
