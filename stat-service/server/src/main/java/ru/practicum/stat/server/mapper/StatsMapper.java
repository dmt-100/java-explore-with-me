package ru.practicum.stat.server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.server.entity.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class StatsMapper {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public EndpointHitDto toDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .app(endpointHit.getApp())
                .ip(endpointHit.getIp())
                .uri(endpointHit.getUri())
                .timestamp(endpointHit.getTimestamp().format(formatter))
                .build();
    }

    public EndpointHit toEntity(EndpointHitDto dto) {
        return EndpointHit.builder()
                .uri(dto.getUri())
                .app(dto.getApp())
                .timestamp(LocalDateTime.parse(dto.getTimestamp(), formatter))
                .ip(dto.getIp())
                .build();
    }
}
