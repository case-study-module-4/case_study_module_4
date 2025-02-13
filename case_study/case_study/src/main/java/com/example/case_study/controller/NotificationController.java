package com.example.case_study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * API để gửi thông báo tùy chỉnh tới một user
     */
    @PostMapping("/send")
    public void sendNotification(@RequestParam("username") String username,
                                 @RequestParam("content") String content) {
        // Gửi thông báo tới user qua WebSocket
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", content);
    }

    /**
     * API được gọi khi một bài đăng được xem
     */
    @PostMapping("/posts/{postId}/view")
    public void viewPost(@PathVariable Integer postId) {
        // Giả lập lấy username chủ bài đăng
        String postOwnerUsername = getPostOwnerUsername(postId);

        // Nội dung thông báo
        String notificationContent = "Bài đăng của bạn vừa được xem!";

        // Gửi thông báo qua WebSocket
        messagingTemplate.convertAndSendToUser(postOwnerUsername, "/queue/notifications", notificationContent);
    }

    /**
     * Giả lập lấy username chủ bài đăng (thực tế cần query DB)
     */
    private String getPostOwnerUsername(Integer postId) {
        // Logic lấy username từ postId (cần thay bằng query thực tế)
        return "username_chu_bai_dang"; // Thay thế bằng query từ database
    }
}
