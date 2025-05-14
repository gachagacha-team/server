package gachagacha.gachaapi.notification;

import gachagacha.common.exception.ErrorCode;
import gachagacha.common.exception.customException.BusinessException;
import gachagacha.domain.item.Item;
import gachagacha.domain.item.ItemGrade;
import gachagacha.domain.notification.Notification;
import gachagacha.domain.notification.NotificationRepository;
import gachagacha.domain.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationProcessor {

    private final NotificationRepository notificationRepository;

    public Notification findById(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_NOTIFICATION));
    }

    public Long saveLottoIssuedNotification(Long userId, ItemGrade itemGrade) {
        String notificationMessage = NotificationType.LOTTO_ISSUED.generateNotificationMessageByLottoIssued(itemGrade);
        return notificationRepository.save(new Notification(null, userId, notificationMessage, NotificationType.LOTTO_ISSUED));
    }

    public Long saveTradeCompletedNotification(Long userId, Item item) {
        String notificationMessage = NotificationType.TRADE_COMPLETED.generateNotificationMessageByTradeCompleted(item);
        return notificationRepository.save(new Notification(null, userId, notificationMessage, NotificationType.TRADE_COMPLETED));
    }
}
