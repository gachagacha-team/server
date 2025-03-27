package gachagacha.gachagacha.api;

import gachagacha.gachagacha.api.dto.request.NotificationReadMarkRequest;
import gachagacha.gachagacha.api.dto.response.NotificationsResponse;
import gachagacha.gachagacha.api.response.ApiResponse;
import gachagacha.gachagacha.domain.notification.Notification;
import gachagacha.gachagacha.domain.notification.NotificationService;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.domain.user.UserService;
import gachagacha.gachagacha.jwt.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final NotificationService notificationService;

    @Operation(summary = "알림 내역 조회")
    @GetMapping("/notifications")
    public ApiResponse<NotificationsResponse> readNotifications(HttpServletRequest request) {
        User user = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        List<Notification> notifications = notificationService.readNotifications(user);

        boolean hasNewNotification = false;
        Optional<Long> optionalLong = notificationService.getLastReadNotificationId(user);
        if (!notifications.isEmpty()) {
            if (optionalLong.isEmpty() || notifications.getFirst().getId() > optionalLong.get()) {
                hasNewNotification = true;
            }
        }
        return ApiResponse.success(NotificationsResponse.of(hasNewNotification, notifications));
    }

    @Operation(summary = "알림 읽음 처리")
    @PutMapping("/notifications/mark")
    public ApiResponse readOneNotification(@RequestBody NotificationReadMarkRequest requestDto, HttpServletRequest request) {
        User user = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        notificationService.markLastReadNotification(requestDto.getNotificationId(), user);
        return ApiResponse.success();
    }
}
