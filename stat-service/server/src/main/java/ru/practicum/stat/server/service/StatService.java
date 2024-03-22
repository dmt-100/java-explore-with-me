package ru.practicum.stat.server.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;
import ru.practicum.stat.server.repository.StatRepository;
import ru.practicum.stat.server.entity.EndpointHit;
import ru.practicum.stat.server.mapper.StatsMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatService {
    StatRepository statRepository;

    public EndpointHitDto addHit(EndpointHitDto endpointHitDto) {
        EndpointHit entity = StatsMapper.toEntity(endpointHitDto);
        return StatsMapper.toDto(
                statRepository.save(entity)
        );
    }

    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (unique)
            return statRepository.getUniqueViewStats(start, end, uris);
        else
            return statRepository.getViewStats(start, end, uris);
    }
}
