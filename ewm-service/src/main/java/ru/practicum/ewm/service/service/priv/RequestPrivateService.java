package ru.practicum.ewm.service.service.priv;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.controller.advice.exception.NotFoundException;
import ru.practicum.ewm.service.controller.advice.exception.ValidationException;
import ru.practicum.ewm.service.dto.event.enums.State;
import ru.practicum.ewm.service.dto.participation.ParticipationResponseDto;
import ru.practicum.ewm.service.dto.participation.enums.ParticipationStatus;
import ru.practicum.ewm.service.entity.Event;
import ru.practicum.ewm.service.entity.Request;
import ru.practicum.ewm.service.entity.User;
import ru.practicum.ewm.service.repository.EventRepository;
import ru.practicum.ewm.service.repository.RequestRepository;
import ru.practicum.ewm.service.repository.UserRepository;
import ru.practicum.ewm.service.util.mapper.RequestMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class RequestPrivateService {
    RequestRepository requestRepository;
    EventRepository eventRepository;
    UserRepository userRepository;

    public List<ParticipationResponseDto> getUserRequests(Long userId) {
        return RequestMapper.toDtoList(requestRepository.findAllByRequesterId(userId));
    }

    @Transactional
    public ParticipationResponseDto addNewRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("no such user"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("no such event"));
        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ValidationException("request already exist");
        }
        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ValidationException("event already filled");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("event not published");
        }
        if (user.equals(event.getInitiator())) {
            throw new ValidationException("initiator can't be requester");
        }
        ParticipationStatus status;
        if (!event.getRequestModeration() || event.getParticipantLimit().equals(0)) {
            status = ParticipationStatus.CONFIRMED;
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            status = ParticipationStatus.PENDING;
        }
        Request request = Request.builder()
                .event(event)
                .requester(user)
                .status(status)
                .created(LocalDateTime.now())
                .build();
        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Transactional
    public ParticipationResponseDto cancelRequest(Long userId, Long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("no such user");
        }
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("no such request"));
        Event event = request.getEvent();
        if (request.getStatus().equals(ParticipationStatus.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        }
        request.setStatus(ParticipationStatus.CANCELED);
        return RequestMapper.toDto(request);
    }
}
