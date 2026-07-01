package com.example.complaintEscalation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.complaintEscalation.dto.NotificationResponseDTO;
import com.example.complaintEscalation.service.NotificationService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping("/user/{userId}")
    public List<NotificationResponseDTO> getNotifications(@PathVariable int userId){
        return notificationService.getNotifications(userId);
    }
    //get unread count
    @GetMapping("/user/{userId}/unread-count")
    public int unreadCount(@PathVariable int userId){
        return notificationService.getUnreadCount(userId);
    }
    //mark as unread
    @PutMapping("/user/{userId}/mark-all-read")
    public String markAllRead(@PathVariable int userId){
        notificationService.markAllRead(userId);
        return "All Marked as read";
    }
}
