package ru.practicum.ewm.service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.service.dto.participation.enums.ParticipationStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    User requester;
    @Enumerated(EnumType.STRING)
    ParticipationStatus status;

    @PrePersist
    public void prePersist() {
        created = LocalDateTime.now();
    }
}
