package com.example.complaintEscalation.dto;

import java.time.LocalDateTime;

import com.example.complaintEscalation.enums.NotificationType;

public class NotificationResponseDTO {
    private Long id;
    private String message;
    private NotificationType type;
    private Integer complaintId;
    private boolean isRead;
    private LocalDateTime createdAt;

    public NotificationResponseDTO(Long id, String message, NotificationType type, Integer complaintId, boolean isRead, LocalDateTime createdAt){
        this.id = id;
        this.message = message;
        this.type = type;
        this.complaintId = complaintId;
        this.isRead = isRead;
        this.createdAt = createdAt;  
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Integer getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Integer complaintId) {
        this.complaintId = complaintId;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }



}
