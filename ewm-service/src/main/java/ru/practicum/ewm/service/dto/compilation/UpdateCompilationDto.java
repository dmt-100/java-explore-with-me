package ru.practicum.ewm.service.dto.compilation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationDto {
    Set<Long> events;
    Boolean pinned;
    @Size(max = 50)
    String title;
}
