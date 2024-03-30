package ru.practicum.ewm.service.controller.priv;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.event.NewEventDto;
import ru.practicum.ewm.service.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.service.dto.participation.ParticipationResponseDto;
import ru.practicum.ewm.service.dto.participation.ParticipationUpdateDto;
import ru.practicum.ewm.service.dto.participation.ParticipationUpdateResponse;
import ru.practicum.ewm.service.service.priv.EventPrivateService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class EventPrivateController {
    EventPrivateService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addNewEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto dto) {
        log.info("Add new event {} {}", userId, dto);
        return service.addNewEvent(userId, dto);
    }

    @GetMapping
    public List<EventFullDto> getEventsByUserId(@PathVariable @NotNull Long userId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Get events by user id {} {} {}", userId, from, size);
        return service.getEventsByUserId(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByEventId(@PathVariable @NotNull Long userId,
                                          @PathVariable @NotNull Long eventId) {
        log.info("Get event by id userId = {} eventId = {}", userId, eventId);
        return service.getEventByEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @Transactional
    public EventFullDto patchEventByEventId(@PathVariable @NotNull Long userId,
                                            @PathVariable @NotNull Long eventId,
                                            @RequestBody @Valid UpdateEventUserRequest dto) {
        log.info("Patch event userId {}, eventId {}, dto {}", userId, eventId, dto);
        return service.patchEvent(userId, eventId, dto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationResponseDto> getRequestsInUserEvent(@PathVariable @NotNull Long userId,
                                                                 @PathVariable @NotNull Long eventId) {
        log.info("Get requests in user event userId {}, eventId {}", userId, eventId);
        return service.getRequestsInUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @Transactional
    public ParticipationUpdateResponse updateEventRequest(@PathVariable @NotNull Long userId,
                                                          @PathVariable @NotNull Long eventId,
                                                          @RequestBody ParticipationUpdateDto dto) {
        log.info("Update event request userId {}, eventId {}, dto {}", userId, eventId, dto);
        return service.updateEventRequest(userId, eventId, dto);
    }

    @GetMapping("/returned")
    public List<EventFullDto> getReturnedEvents(@PathVariable @NotNull Long userId) {
        log.info("Get returned events for user {}", userId);
        return service.getReturnedEvents(userId);
    }
}
