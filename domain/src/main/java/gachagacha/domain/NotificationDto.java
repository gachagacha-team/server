package gachagacha.domain;

import gachagacha.domain.item.Item;
import gachagacha.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationDto {

    private User seller;
    private Item item;
    private Long notificationId;
}
