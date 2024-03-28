package ru.practicum.ewm.service.controller.publ;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.event.enums.Sorted;
import ru.practicum.ewm.service.service.publ.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.service.util.DefaultValues.DATETIME_PATTERN;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/events")
@Slf4j
public class EventPublicController {
    EventPublicService eventPublicService;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) String text,
                                        @RequestParam(required = false) List<Integer> categories,
                                        @RequestParam(required = false) Boolean paid,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(pattern = DATETIME_PATTERN) LocalDateTime rangeStart,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(pattern = DATETIME_PATTERN) LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                        @RequestParam(required = false) Sorted sorted,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        HttpServletRequest request) {
        log.info("Get events {} {} {} {} {} {} {}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sorted);
        return eventPublicService.getEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sorted, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Get event with id {}", id);
        return eventPublicService.getEventById(id, request);
    }
}
