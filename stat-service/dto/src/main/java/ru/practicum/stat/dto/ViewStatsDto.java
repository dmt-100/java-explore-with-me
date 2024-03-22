package ru.practicum.stat.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Data
public class ViewStatsDto {
    String app;
    String uri;
    Long hits;
}
