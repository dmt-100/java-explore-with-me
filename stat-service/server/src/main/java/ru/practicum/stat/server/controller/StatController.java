package ru.practicum.stat.server.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;
import ru.practicum.stat.server.service.StatService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class StatController {
    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    StatService statService;

    @RequestMapping(method = RequestMethod.GET, path = "/stats")
    public List<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = DATETIME_PATTERN) LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = DATETIME_PATTERN) LocalDateTime end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Get stats, start = {}, end = {}", start, end);
        return statService.getViewStats(start, end, uris, unique);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/hit")
    @Async
    public CompletableFuture<EndpointHitDto> addHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("Add hit, endpoint hit = {}", endpointHitDto);
        return CompletableFuture.completedFuture(statService.addHit(endpointHitDto));
    }
}
