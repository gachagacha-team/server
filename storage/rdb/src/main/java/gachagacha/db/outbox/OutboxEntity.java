package gachagacha.db.outbox;

import gachagacha.domain.outbox.Outbox;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "outbox")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outbox_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String payload;

    public static OutboxEntity create(String topic, String payload) {
        OutboxEntity outbox = new OutboxEntity();
        outbox.topic = topic;
        outbox.payload = payload;
        return outbox;
    }

    public static OutboxEntity fromOutbox(Outbox outbox) {
        return new OutboxEntity(outbox.getId(), outbox.getTopic(), outbox.getPayload());
    }

    public Outbox toOutbox() {
        return new Outbox(id, topic, payload);
    }
}

