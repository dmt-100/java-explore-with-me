package ru.practicum.ewm.service.controller.priv;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.participation.ParticipationResponseDto;
import ru.practicum.ewm.service.service.priv.RequestPrivateService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RequestPrivateController {
    RequestPrivateService requestService;

    @GetMapping
    public List<ParticipationResponseDto> getUserRequests(@PathVariable Long userId) {
        log.info("Get user requests {}", userId);
        return requestService.getUserRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationResponseDto addNewRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Add new request with userId {} and eventId {}", userId, eventId);
        return requestService.addNewRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationResponseDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Cancel request, userId {}, requestId {}", userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }
}
