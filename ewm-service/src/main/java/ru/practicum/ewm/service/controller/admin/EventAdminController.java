package ru.practicum.ewm.service.controller.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.service.dto.event.enums.State;
import ru.practicum.ewm.service.service.admin.EventAdminService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.service.util.DefaultValues.DATETIME_PATTERN;

@RestController
@RequestMapping("/admin/events")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventAdminController {
    EventAdminService eventService;

    @GetMapping
    public List<EventFullDto> searchEvents(@RequestParam(required = false) List<Long> users,
                                           @RequestParam(required = false) State states,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(pattern = DATETIME_PATTERN) LocalDateTime rangeStart,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(pattern = DATETIME_PATTERN) LocalDateTime rangeEnd,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Search events {} {} {} {} {}", users, states, categories, rangeStart, rangeEnd);
        return eventService.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @Transactional
    public EventFullDto confirmOrRejectEvent(@PathVariable Long eventId,
                                             @Valid @RequestBody UpdateEventAdminRequest request) {
        log.info("Confirm Or Reject Event {} {}", eventId, request);
        return eventService.confirmOrRejectEvent(eventId, request);
    }
}
