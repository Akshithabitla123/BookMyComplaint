package com.example.complaintEscalation.dto;

import com.example.complaintEscalation.enums.ComplaintStatus;
import com.example.complaintEscalation.enums.Role;

import java.time.LocalDate;

public class DetailsDto {
    private String userName;
    private String email;
    private Role role;
    private String title;
    private String description;
    private String area;
    private ComplaintStatus status;
    private String assignedStaff;
    private LocalDate createdAt;
    private LocalDate updatedAt;


    public DetailsDto(String userName, String email, Role role, String title, String description, String area, ComplaintStatus status,String assignedStaff, LocalDate createdAt, LocalDate updatedAt) {
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.title = title;
        this.description = description;
        this.area = area;
        this.status = status;
        this.assignedStaff = assignedStaff;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }

    public String getUserName() {
        return userName;
    }

      public String getEmail() {
        return email;
    }
    public Role getRole() {
        return role;
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }

    public String getArea() {
        return area;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public String getAssignedStaff() {
        return assignedStaff;
    }

}
