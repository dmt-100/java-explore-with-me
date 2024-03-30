package ru.practicum.ewm.service.dto.participation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationUpdateResponse {
    List<ParticipationResponseDto> confirmedRequests;
    List<ParticipationResponseDto> rejectedRequests;
}
