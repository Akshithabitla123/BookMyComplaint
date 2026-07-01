package com.example.complaintEscalation.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.complaintEscalation.dto.DetailsDto;
import com.example.complaintEscalation.enums.ComplaintStatus;
import com.example.complaintEscalation.enums.Priority;
import com.example.complaintEscalation.model.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint,Integer> {

    Page<Complaint> findByTitleContaining(String title,Pageable pageable);
    Page<Complaint> findByStatus(ComplaintStatus status,Pageable pageable);
    Page<Complaint> findByUser_userId(int userId,Pageable pageable);
    Page<Complaint> findByAreaIgnoreCase(String area,Pageable pageable);
    List<Complaint> findByCreatedAtBetween(LocalDateTime from,LocalDateTime to);

    List<Complaint> findByStatusAndPriorityAndCreatedAtBefore(ComplaintStatus status, Priority priority, LocalDate cutoffDate);

    @Query("""
        SELECT new com.example.complaintEscalation.dto.DetailsDto(
            u.userName,
            u.email,
            u.role,
            c.title,
            c.description,
            c.area,
            c.status,
            c.assignedStaff,
            c.createdAt,
            c.updatedAt
           
        )
        FROM Complaint c
        JOIN user u
        WHERE c.complaintId = :id
    """)
    DetailsDto findDetails(int id);

    //counting no. of complaints per user which are open
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.user.userId=:userId "+
        "AND c.status IN ('OPEN','IN_PROGRESS','ESCALATED')")
    int countActiveComplaintsByUser(@Param("userId") int userId);


}
