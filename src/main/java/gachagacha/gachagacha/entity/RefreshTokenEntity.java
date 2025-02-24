package gachagacha.gachagacha.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private long id;
    
    private String refreshToken;

    @Column(nullable = false, columnDefinition = "DATETIME(0)")
    private LocalDateTime expires_at;

    public RefreshTokenEntity(String refreshToken) {
        this.refreshToken = refreshToken;
        this.expires_at = LocalDateTime.now().plusDays(30l);
    }
}
