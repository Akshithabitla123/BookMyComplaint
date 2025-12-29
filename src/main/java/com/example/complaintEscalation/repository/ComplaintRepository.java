package com.example.complaintEscalation.repository;

import com.example.complaintEscalation.enums.ComplaintStatus;
import com.example.complaintEscalation.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint,Integer> {

    List<Complaint> findByTitleOrDescriptionContaining(String title, String description);
    List<Complaint> findByStatus(ComplaintStatus status);
}
