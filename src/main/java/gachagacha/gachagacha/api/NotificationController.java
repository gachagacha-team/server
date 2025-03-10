package gachagacha.gachagacha.api;

import gachagacha.gachagacha.api.dto.response.NotificationsResponse;
import gachagacha.gachagacha.api.response.ApiResponse;
import gachagacha.gachagacha.domain.notification.Notification;
import gachagacha.gachagacha.domain.notification.NotificationService;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.user.UserService;
import gachagacha.gachagacha.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ApiResponse<NotificationsResponse> readNotifications(HttpServletRequest request) {
        User user = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        List<Notification> notifications = notificationService.readNotifications(user);
        List<NotificationsResponse.NotificationDto> notificationDtos = notifications.stream()
                .map(notification -> {
                    boolean isRead = notificationService.isRead(notification.getId());
                    return new NotificationsResponse.NotificationDto(notification.getId(), notification.getType(), isRead, notification.getData());
                })
                .toList();
        return ApiResponse.success(NotificationsResponse.of(notificationDtos));
    }

    @PatchMapping("/notifications/{notificationId}")
    public ApiResponse readOneNotification(@PathVariable long notificationId, HttpServletRequest request) {
        User user = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        notificationService.readOneNotification(notificationId, user);
        return ApiResponse.success();
    }
}
