package com.SWP.WebServer.controller;

import com.SWP.WebServer.entity.Notification;
import com.SWP.WebServer.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NotificationController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationService notificationService;

//    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/hello")
    public void sendNotification(int userId, String message) {
        String userID = String.valueOf(userId);
        messagingTemplate.convertAndSend("/topic/notifications/" + userID, message);
    }

    @GetMapping("/get-notif")
    public ResponseEntity<?> getNotifications() {
        List<Notification> notifications = notificationService.getAll();
        return ResponseEntity.ok(notifications);
    }
}
