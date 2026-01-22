package com.example.complaintEscalation.repository;

import com.example.complaintEscalation.enums.ComplaintStatus;
import com.example.complaintEscalation.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint,Integer> {

    List<Complaint> findByTitleContaining(String title);
    List<Complaint> findByStatus(ComplaintStatus status);
    List<Complaint> findByUser_userId(int userId);
    List<Complaint> findByAreaIgnoreCase(String area);
    List<Complaint> findByCreatedAtBetween(LocalDateTime from,LocalDateTime to);

}
