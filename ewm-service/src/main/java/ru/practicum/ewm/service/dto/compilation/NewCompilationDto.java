package ru.practicum.ewm.service.dto.compilation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {
    Set<Long> events;
    @Builder.Default
    Boolean pinned = false;
    @NotBlank
    @Size(max = 50)
    String title;
}
