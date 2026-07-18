package com.example.demo.controller;

import com.example.demo.config.UserContext;
import com.example.demo.model.Notification;
import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(notificationRepository.findByUserIdOrderByCreatedAtDesc(userId));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return ResponseEntity.ok(0L);
        }
        return ResponseEntity.ok(notificationRepository.countByUserIdAndIsReadFalse(userId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        Optional<Notification> notifOpt = notificationRepository.findById(id);
        if (!notifOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Notification notification = notifOpt.get();
        Long userId = UserContext.getUserId();
        if (userId == null || !userId.equals(notification.getUserId())) {
            return ResponseEntity.status(403).body("Unauthorized to modify this notification");
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
        return ResponseEntity.ok().build();
    }
}
