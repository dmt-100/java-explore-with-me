package ru.practicum.stat.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stat.dto.ViewStatsDto;
import ru.practicum.stat.server.entity.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;


public interface StatRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.stat.dto.ViewStatsDto(e.app, e.uri, COUNT(e))  " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "AND ((:uris) IS NULL OR e.uri IN :uris) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e) DESC")
    List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.stat.dto.ViewStatsDto(e.app, e.uri, COUNT(DISTINCT e.ip))  " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "AND ((:uris) IS NULL OR e.uri IN :uris) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e) DESC")
    List<ViewStatsDto> getUniqueViewStats(LocalDateTime start, LocalDateTime end, List<String> uris);
}
