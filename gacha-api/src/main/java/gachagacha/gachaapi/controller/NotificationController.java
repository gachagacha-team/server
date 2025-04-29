package gachagacha.gachaapi.controller;

import gachagacha.gachaapi.dto.response.HasNewNotificationResponse;
import gachagacha.gachaapi.dto.response.NotificationsResponse;
import gachagacha.gachaapi.response.ApiResponse;
import gachagacha.gachaapi.jwt.JwtUtils;
import gachagacha.domain.notification.Notification;
import gachagacha.gachaapi.service.NotificationService;
import gachagacha.domain.user.User;
import gachagacha.gachaapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final NotificationService notificationService;

    @Operation(summary = "새로운 알림 유무 조회")
    @GetMapping("/notifications/has_new")
    public ApiResponse<HasNewNotificationResponse> hasNewNotification(HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        boolean hasNewNotification = notificationService.hasNewNotification(user);
        return ApiResponse.success(HasNewNotificationResponse.of(hasNewNotification));
    }

    @Operation(summary = "알림 내역 조회")
    @GetMapping("/notifications")
    public ApiResponse<NotificationsResponse> readNotifications(HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        List<Notification> notifications = notificationService.readRecentNotifications(user);
        return ApiResponse.success(NotificationsResponse.of(notifications));
    }
}
