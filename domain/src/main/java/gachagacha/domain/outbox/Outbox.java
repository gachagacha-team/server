package gachagacha.domain.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Outbox {

    private Long id;
    private String topic;
    private String payload;
}

