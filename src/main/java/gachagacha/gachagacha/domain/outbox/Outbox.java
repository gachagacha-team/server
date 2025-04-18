package gachagacha.gachagacha.domain.outbox;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outbox_id", nullable = false)
    private long id;

    @Column(nullable = false)
    private String topic;

    @Lob
    @Column(nullable = false)
    private String payload;

    public static Outbox create(String topic, String payload) {
        Outbox outbox = new Outbox();
        outbox.topic = topic;
        outbox.payload = payload;
        return outbox;
    }
}

