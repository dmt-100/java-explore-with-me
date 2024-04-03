package ru.practicum.ewm.service.dto.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAfterModerationDto {
    @NotBlank
    String moderatorComment;
}
