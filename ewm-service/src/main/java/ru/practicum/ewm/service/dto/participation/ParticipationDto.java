package ru.practicum.ewm.service.dto.participation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.service.dto.participation.enums.ParticipationStatus;

import static ru.practicum.ewm.service.util.DefaultValues.DATETIME_PATTERN;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationDto {
    Long id;
    @JsonFormat(pattern = DATETIME_PATTERN)
    String created;
    Long event;
    Long requester;
    ParticipationStatus status;
}
