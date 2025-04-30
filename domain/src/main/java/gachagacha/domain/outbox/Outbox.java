package gachagacha.domain.outbox;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Outbox {

    private Long id;
    private String topic;
    private String payload;

    public static Outbox of(String topic, String payload) {
        return new Outbox(null, topic, payload);
    }
}

