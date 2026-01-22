package com.example.complaintEscalation.controller;


import com.example.complaintEscalation.enums.ComplaintStatus;
import com.example.complaintEscalation.model.Complaint;
import com.example.complaintEscalation.model.User;
import com.example.complaintEscalation.service.ComplaintService;
import com.example.complaintEscalation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
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
        complaint.setCreatedAt(LocalDate.now());
        complaintService.save(complaint);
    }

    //return all complaints of user
    @GetMapping("/user/{id}")
    public List<Complaint> getComplaints(@PathVariable int id){
        return complaintService.getById(id);
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

    //filter complaints
    @GetMapping("/admin/filter/area")
    public List<Complaint> filterByArea(@RequestParam String area){
        return complaintService.findByArea(area);
    }
    @GetMapping("/admin/filter/status")
    public List<Complaint> filterByStatus(@RequestParam ComplaintStatus status){
        return complaintService.findByStatus(status);
    }
    //get complaints by title or description
    @GetMapping("/admin/filter/title")
    public List<Complaint> getComplaintsByTitle(@RequestParam String title){
        return complaintService.findByTitle(title);
    }



}
