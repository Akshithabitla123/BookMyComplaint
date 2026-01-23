package com.example.complaintEscalation.repository;

import com.example.complaintEscalation.dto.DetailsDto;
import com.example.complaintEscalation.enums.ComplaintStatus;
import com.example.complaintEscalation.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint,Integer> {

    List<Complaint> findByTitleContaining(String title);
    List<Complaint> findByStatus(ComplaintStatus status);
    List<Complaint> findByUser_userId(int userId);
    List<Complaint> findByAreaIgnoreCase(String area);
    List<Complaint> findByCreatedAtBetween(LocalDateTime from,LocalDateTime to);


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

}
