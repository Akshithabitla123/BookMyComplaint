package com.example.complaintEscalation.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.complaintEscalation.dto.NotificationResponseDTO;
import com.example.complaintEscalation.enums.NotificationType;
import com.example.complaintEscalation.model.Notification;
import com.example.complaintEscalation.model.User;
import com.example.complaintEscalation.repository.NotificationRepo;
import com.example.complaintEscalation.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepo notificationRepo;
    private final UserRepository userRepo;

    //creates and saves notification
    public void createNotification(User user,String message,NotificationType type, Integer complaintId){
        Notification noti=new Notification();
        noti.setUser(user);
        noti.setMessage(message);
        noti.setType(type);
        noti.setComplaintId(complaintId);
        noti.setIsRead(false);
        noti.setCreatedAt(LocalDateTime.now());
        notificationRepo.save(noti);
    }

    //get all notifications of a user
    public List<NotificationResponseDTO> getNotifications(int userId){
        return notificationRepo.findByUserUserIdOrderByCreatedAtDesc(userId)
            .stream()
            .map(n->new NotificationResponseDTO(
                n.getId(),
                n.getMessage(),
                n.getType(),
                n.getComplaintId(),
                n.isIsRead(),
                n.getCreatedAt()
            )).toList();
    }

    //unread count of notifications of user
    public int getUnreadCount(int userId){
        return notificationRepo.countByUserUserIdAndIsReadFalse(userId);
    }

    //mark all as read
    public void markAllRead(int userId){
        List<Notification> unread=notificationRepo.findByUserUserIdAndIsReadFalse(userId);
        unread.forEach(n->n.setIsRead(true));
        notificationRepo.saveAll(unread);
    }
}
