package com.example.complaintEscalation.model;


import java.time.LocalDate;

import com.example.complaintEscalation.enums.ComplaintStatus;
import com.example.complaintEscalation.enums.Priority;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int complaintId;
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message="Description is required")
    private String description;
    @NotBlank(message="Area is required")
    private String area;

    @Enumerated(EnumType.STRING)
    private ComplaintStatus status;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate escalatedAt;
    private String assignedStaff;


    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }


    public String getAssignedStaff() {
        return assignedStaff;
    }

    public void setAssignedStaff(String assignedStaff) {
        this.assignedStaff = assignedStaff;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDate getEscalatedAt() {
        return escalatedAt;
    }

    public void setEscalatedAt(LocalDate escalatedAt) {
        this.escalatedAt = escalatedAt;
    }
    
    
}
