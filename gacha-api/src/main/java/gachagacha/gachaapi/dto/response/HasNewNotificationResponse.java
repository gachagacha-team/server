package gachagacha.gachaapi.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HasNewNotificationResponse {

    private boolean hasNewNotification;

    public static HasNewNotificationResponse of(boolean hasNewNotification) {
        return new HasNewNotificationResponse(hasNewNotification);
    }
}
