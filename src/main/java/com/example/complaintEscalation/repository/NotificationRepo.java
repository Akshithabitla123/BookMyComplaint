package com.example.complaintEscalation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.complaintEscalation.model.Notification;

public interface  NotificationRepo extends JpaRepository<Notification,Long>{
    //all notifications of a user (newest first)
    List<Notification> findByUserUserIdOrderByCreatedAtDesc(int userId);
    //unread count-> to show on the bell icon
    int countByUserUserIdAndIsReadFalse(int userId);
    //for mark all as read
    List<Notification> findByUserUserIdAndIsReadFalse(int userId);
}
