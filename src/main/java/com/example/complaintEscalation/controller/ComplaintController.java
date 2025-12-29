package com.example.complaintEscalation.controller;


import com.example.complaintEscalation.enums.ComplaintStatus;
import com.example.complaintEscalation.model.Complaint;
import com.example.complaintEscalation.model.User;
import com.example.complaintEscalation.service.ComplaintService;
import com.example.complaintEscalation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {
    @Autowired
    private UserService userService;

    @Autowired
    private ComplaintService complaintService;

    //citizen creates a complaint
    @PostMapping("/user/{id}")
    public void createComplaint(@PathVariable int id, @RequestBody Complaint complaint){
        User user=userService.getUserById(id);
        complaint.setUser(user);
        complaint.setStatus(ComplaintStatus.OPEN);
        complaint.setCreatedAt(LocalDateTime.now());
        complaintService.save(complaint);
    }

    //get all complaints
    @GetMapping("/complaints")
    public List<Complaint> getAllComplaints(){
        return complaintService.getAllComplaints();
    }

    //get complaints by title or description
    @GetMapping("/search")
    public List<Complaint> getComplaintsByTitleOrDescription(@RequestParam String title,@RequestParam String description){
        return complaintService.getByTitleOrDescription(title, description);
    }

    //get complaints by status
    @GetMapping("/status")
    public List<Complaint> getComplaintsByStatus(@RequestParam ComplaintStatus status){
        return complaintService.getByStatus(status);
    }

}
