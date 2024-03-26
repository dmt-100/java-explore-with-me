package ru.practicum.ewm.service.controller.publ;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.event.enums.AvailableValues;
import ru.practicum.ewm.service.service.publ.EventPublicService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/events")
public class EventPublicController {
    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
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
                                        @RequestParam(required = false) AvailableValues sort,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return eventPublicService.getEvents(text,categories,paid,rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Long id) {
        return eventPublicService.getEventById(id);
    }
}
