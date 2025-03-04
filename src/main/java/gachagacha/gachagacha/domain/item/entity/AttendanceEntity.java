package gachagacha.gachagacha.domain.item.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "attendance")
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    private LocalDate date;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private int bonusCoin;
}
