package com.example.complaintEscalation.controller;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.complaintEscalation.dto.DetailsDto;
import com.example.complaintEscalation.enums.ComplaintStatus;
import com.example.complaintEscalation.enums.Priority;
import com.example.complaintEscalation.exceptions.InvalidRequestException;
import com.example.complaintEscalation.model.Complaint;
import com.example.complaintEscalation.model.User;
import com.example.complaintEscalation.service.ComplaintService;
import com.example.complaintEscalation.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/complaints")
@RequiredArgsConstructor
public class ComplaintController {
    private final UserService userService;
    private final ComplaintService complaintService;

    //citizen creates a complaint
    @PostMapping("/user/{id}")
    public ResponseEntity<Map<String,String>> createComplaint(@PathVariable int id, @Valid @RequestBody Complaint complaint){
        User user=userService.getUserById(id);
        //check count of complaints of current user
        int activeComplaints=complaintService.countActiveComplaints(id);
        if(activeComplaints>=5){
            throw new InvalidRequestException("You have reached the maximum limit of 5 active complaints. "+
                "Please wait for your existing complaints to be resolved before submitting new ones."
            );

        }
        complaint.setUser(user);
        complaint.setStatus(ComplaintStatus.OPEN);
        complaint.setCreatedAt(LocalDate.now());
        if(complaint.getPriority()==null){
            complaint.setPriority(Priority.MEDIUM);
        }
        complaintService.save(complaint);
        return ResponseEntity.ok(Map.of("message","Complaint submitted successfully"));
    }

    //return all complaints of user
    @GetMapping("/user/{id}")
    public Page<Complaint> getComplaints(@PathVariable int id,Pageable pageable){
        return complaintService.getById(id,pageable);
    }


    //below APIs only for admins
    //get all complaints
    @GetMapping("/admin/complaints")
    public List<Complaint> getAllComplaints(){
        return complaintService.getAllComplaints();
    }

    //update status
    @PutMapping("/admin/{id}/status")
    public ResponseEntity<Complaint> updateStatus(@PathVariable int id,@RequestParam ComplaintStatus status){
        return ResponseEntity.ok(complaintService.updateStatus(id,status));
    }

    //Assign staff (complaint id)
    @PutMapping("/admin/{id}/assign")
    public ResponseEntity<Complaint> assignStaff(@PathVariable int id,@RequestParam String staffName){
        return ResponseEntity.ok(complaintService.assignStaff(id,staffName));
    }
    //review the escalted complaints and change the status to IN_PROGRESS
    @PutMapping("/admin/{id}/review")
    public ResponseEntity<Complaint> reviewEscalation(@PathVariable int id){
        return ResponseEntity.ok(complaintService.reviewEscalation(id));
    }
    //filter complaints
    @GetMapping("/admin/filter/area")
    public Page<Complaint> filterByArea(@RequestParam String area,Pageable pageable){
        return complaintService.findByArea(area,pageable);
    }
    @GetMapping("/admin/filter/status")
    public Page<Complaint> filterByStatus(@RequestParam ComplaintStatus status,Pageable pageable){
        return complaintService.findByStatus(status,pageable);
    }
    //get complaints by title or description
    @GetMapping("/admin/filter/title")
    public Page<Complaint> getComplaintsByTitle(@RequestParam String title,Pageable pageable){
        return complaintService.findByTitle(title,pageable);
    }
    //get detailsDto of complaint
    @GetMapping("/admin/details/{id}")
    public DetailsDto getDetails(@PathVariable int id){
        return complaintService.getDetails(id);
    }

    //manually triggring auto escalation (for testing)
    @PostMapping("/admin/escalate/run")
    public ResponseEntity<Map<String,Integer>> runEscalation(){
        int count=complaintService.runEscalation();
        return ResponseEntity.ok(Map.of("escalatedCount",count));
    }

}
