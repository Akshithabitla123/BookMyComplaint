package com.example.complaintEscalation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.complaintEscalation.dto.DetailsDto;
import com.example.complaintEscalation.enums.ComplaintStatus;
import com.example.complaintEscalation.enums.NotificationType;
import com.example.complaintEscalation.enums.Priority;
import com.example.complaintEscalation.exceptions.InvalidRequestException;
import com.example.complaintEscalation.exceptions.ResourceNotFoundException;
import com.example.complaintEscalation.model.Complaint;
import com.example.complaintEscalation.repository.ComplaintRepository;
import com.example.complaintEscalation.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository complaintRepo;
    private final NotificationService notificationService;
    private final UserRepository userRepo;
    private static final Logger log=LoggerFactory.getLogger(ComplaintService.class);
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
    public Page<Complaint> getById(int id,Pageable pageable){
        return complaintRepo.findByUser_userId(id,pageable);
    }

    //get complaint by title
    public Page<Complaint> findByTitle(String title,Pageable pageable){
        return complaintRepo.findByTitleContaining(title,pageable);
    }

    //update status of complaint
    public Complaint updateStatus(int complaintId,ComplaintStatus status){
        Complaint complaint=complaintRepo.findById(complaintId).orElseThrow(()->new ResourceNotFoundException("Complaint not found"));
        validateStatusTransition(complaint.getStatus(),status);
        complaint.setStatus(status);
        complaint.setUpdatedAt(LocalDate.now());
        return complaintRepo.save(complaint);
    }
    private void validateStatusTransition(ComplaintStatus current, ComplaintStatus next){
        if(current==ComplaintStatus.RESOLVED || current==ComplaintStatus.REJECTED){
            throw new InvalidRequestException("Cannot change status of a complaint that is already "+current);
        }
        if(current==ComplaintStatus.ESCALATED){
            throw new InvalidRequestException("Complaint is Auto-Escalated, Use the review endpoint");
        }
        if(current==next){
            throw new InvalidRequestException("Complaint is already in status "+current);
        }
    }

    //assign staff to the complaint
    public Complaint assignStaff(int complaintId, String staffName){
        if(staffName==null || staffName.isBlank()){
            throw new InvalidRequestException("Staff name must not be empty");
        }
        Complaint complaint=complaintRepo.findById(complaintId).orElseThrow(()->new ResourceNotFoundException("Complaint not found"));
        if(complaint.getStatus()==ComplaintStatus.ESCALATED){
            throw new InvalidRequestException("Complaint "+complaintId+" is Escalated and needs admin review before staff can be assigned");
        }
        if(complaint.getStatus()==ComplaintStatus.REJECTED || complaint.getStatus()==ComplaintStatus.RESOLVED){
            throw new InvalidRequestException("Cannot assign staff");
        }
        complaint.setAssignedStaff(staffName);
        complaint.setUpdatedAt(LocalDate.now());
        complaint.setStatus(ComplaintStatus.IN_PROGRESS);
        return complaintRepo.save(complaint);
    }

    //review auto-escalation
    public Complaint reviewEscalation(int complaintId){
        Complaint complaint=complaintRepo.findById(complaintId).orElseThrow(()->new ResourceNotFoundException("Complaint not found with given Id"));
        if(complaint.getStatus()!=ComplaintStatus.ESCALATED){
            throw new InvalidRequestException("Complaint is not Escalated");
        }
        complaint.setStatus(ComplaintStatus.IN_PROGRESS);
        complaint.setUpdatedAt(LocalDate.now());
        return complaintRepo.save(complaint);
    }
    //filter by area,status, date range
    public Page<Complaint> findByArea(String area,Pageable pageable){
        return complaintRepo.findByAreaIgnoreCase(area,pageable);
    }
    public Page<Complaint> findByStatus(ComplaintStatus status,Pageable pageable){
        return complaintRepo.findByStatus(status,pageable);
    }
    public List<Complaint> findByDate(LocalDateTime from, LocalDateTime to){
        return complaintRepo.findByCreatedAtBetween(from, to);
    }

    //get details of the complaint (dto)
    public DetailsDto getDetails(int id){
        return complaintRepo.findDetails(id);
    }

    //auto escalation
    //runs every hour, escalates open complaints that have been unresolved past their priority-based threshold: high-> 1day, medium>2 days, low-> 3days
    @Scheduled(fixedRate=3600000)
    @Transactional
    public int runEscalation(){
        int escalatedCount=0;
        escalatedCount+=escalateByPriority(Priority.HIGH,1);
        escalatedCount+=escalateByPriority(Priority.MEDIUM,2);
        escalatedCount+=escalateByPriority(Priority.LOW,3);
        log.info("Escalation sweep complete. {} complaints escalted",escalatedCount);
        return escalatedCount;
    }

    private int escalateByPriority(Priority priority, int days){
        LocalDate cutoff=LocalDate.now().minusDays(days);
        List<Complaint> overdue=complaintRepo.findByStatusAndPriorityAndCreatedAtBefore(ComplaintStatus.OPEN, priority, cutoff);
        for(Complaint c:overdue){
            c.setStatus(ComplaintStatus.ESCALATED);
            c.setEscalatedAt(LocalDate.now());
            complaintRepo.save(c);
            String msg="Your complaint "+c.getTitle()+" has been escalated due to no resolution within expected timeframe";
            notificationService.createNotification(c.getUser(), msg,NotificationType.COMPLAINT_ESCALATED,c.getComplaintId());
            //notify all admins
             String adminMsg="Complaint "+c.getComplaintId()+" '"+c.getTitle()+"' ("+c.getPriority() +" priority) has been auto-escalated "+"and requires admin review.";
            userRepo.findByRole("ADMIN").forEach(admin->
                notificationService.createNotification(admin, adminMsg,NotificationType.REVIEW_REQUIRED,c.getComplaintId()));
        }
        return overdue.size();
    }

    //rate limiting-> returns active complaints of user
    public int countActiveComplaints(int userId){
        return complaintRepo.countActiveComplaintsByUser(userId);
    }
}
