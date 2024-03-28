package ru.practicum.ewm.service.dto.participation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.service.dto.participation.enums.ParticipationStatus;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationUpdateDto {
    Set<Long> requestIds;
    ParticipationStatus status;
}
