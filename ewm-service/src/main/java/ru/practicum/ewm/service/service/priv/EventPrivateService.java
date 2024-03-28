package ru.practicum.ewm.service.service.priv;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.controller.advice.exception.BadRequestException;
import ru.practicum.ewm.service.controller.advice.exception.NotFoundException;
import ru.practicum.ewm.service.controller.advice.exception.ValidationException;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.event.NewEventDto;
import ru.practicum.ewm.service.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.service.dto.event.enums.State;
import ru.practicum.ewm.service.dto.event.enums.StateAction;
import ru.practicum.ewm.service.dto.participation.ParticipationResponseDto;
import ru.practicum.ewm.service.dto.participation.ParticipationUpdateDto;
import ru.practicum.ewm.service.dto.participation.ParticipationUpdateResponse;
import ru.practicum.ewm.service.entity.Category;
import ru.practicum.ewm.service.entity.Event;
import ru.practicum.ewm.service.entity.Request;
import ru.practicum.ewm.service.entity.User;
import ru.practicum.ewm.service.repository.CategoryRepository;
import ru.practicum.ewm.service.repository.EventRepository;
import ru.practicum.ewm.service.repository.RequestRepository;
import ru.practicum.ewm.service.repository.UserRepository;
import ru.practicum.ewm.service.util.mapper.EventMapper;
import ru.practicum.ewm.service.util.mapper.RequestMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm.service.dto.participation.enums.ParticipationStatus.*;
import static ru.practicum.ewm.service.util.DefaultValues.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class EventPrivateService {
    private static final int MIN_HOURS_FOR_PUBLICATION = 2;
    EventRepository eventRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;
    RequestRepository requestRepository;

    public static boolean isEventDateCorrect(LocalDateTime eventDate, int minHours) {
        LocalDateTime currentTime = LocalDateTime.now();
        long difference = ChronoUnit.HOURS.between(currentTime, eventDate);
        return difference >= minHours;
    }

    @Transactional
    public EventFullDto addNewEvent(Long userId, NewEventDto dto) {
        LocalDateTime eventDate = LocalDateTime.parse(dto.getEventDate(), DateTimeFormatter.ofPattern(DATETIME_PATTERN));
        if (!isEventDateCorrect(eventDate, MIN_HOURS_FOR_PUBLICATION)) {
            throw new BadRequestException("event date is incorrect");
        }
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("no such user"));
        Category category = categoryRepository.findById(dto.getCategory().longValue())
                .orElseThrow(() -> new NotFoundException("no such category"));
        Event event = EventMapper.toEntity(dto, initiator, category);
        return EventMapper.toDto(eventRepository.save(event));
    }

    public List<EventFullDto> getEventsByUserId(Long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("no such user");
        }
        List<Event> events = eventRepository.findAllByInitiatorIdOrderByCreatedOn(userId, createPage(from, size));
        return EventMapper.toDtoList(events);
    }

    public EventFullDto getEventByEventId(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("no such user");
        }
        Event event = getEventIfExist(eventId);
        return EventMapper.toDto(event);
    }

    @Transactional
    public EventFullDto patchEvent(Long userId, Long eventId, UpdateEventUserRequest dto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("no such user");
        }
        Event event = getEventIfExist(eventId);
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("event is already published");
        }

        if (dto.getEventDate() != null) {
            LocalDateTime newEventDate = LocalDateTime.parse(
                    dto.getEventDate(), DateTimeFormatter.ofPattern(DATETIME_PATTERN));

            if (!isEventDateCorrect(newEventDate, MIN_HOURS_FOR_PUBLICATION)) {
                throw new BadRequestException("event date is incorrect");
            }
        }
        event.setState(State.PENDING);
        if (dto.getStateAction() != null && dto.getStateAction().equals(StateAction.CANCEL_REVIEW))
            event.setState(State.CANCELED);
        BeanUtils.copyProperties(dto, event, getNullPropertyNames(dto));
        return EventMapper.toDto(event);
    }

    public List<ParticipationResponseDto> getRequestsInUserEvent(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("no such user");
        }
        getEventIfExist(eventId);
        return RequestMapper.toDtoList(
                requestRepository.findAllByEventId(eventId));
    }

    @Transactional
    public ParticipationUpdateResponse updateEventRequest(Long userId, Long eventId, ParticipationUpdateDto dto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("no such user");
        }
        Event event = getEventIfExist(eventId);
        Integer participantLimit = event.getParticipantLimit();
        if (event.getConfirmedRequests().equals(participantLimit)) {
            throw new ValidationException("participant limit reached");
        }
        if (participantLimit == 0 || !event.getRequestModeration()) {
            return new ParticipationUpdateResponse();
        }
        List<Request> requests = requestRepository.findAllById(dto.getRequestIds());
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();
        requests.forEach(r -> {
            int freePoints = participantLimit - event.getConfirmedRequests();
            if (!r.getStatus().equals(PENDING)) {
                throw new ValidationException("request must have pending status");
            }
            if (freePoints > 0) {
                if (dto.getStatus().equals(CONFIRMED)) {
                    r.setStatus(CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    confirmedRequests.add(r);
                } else {
                    r.setStatus(REJECTED);
                    rejectedRequests.add(r);
                }
            } else {
                r.setStatus(REJECTED);
                rejectedRequests.add(r);
            }
        });
        eventRepository.saveAndFlush(event);
        List<ParticipationResponseDto> confirmedDto = RequestMapper.toDtoList(confirmedRequests);
        List<ParticipationResponseDto> rejectedDto = RequestMapper.toDtoList(rejectedRequests);
        return new ParticipationUpdateResponse(confirmedDto, rejectedDto);
    }

    private Event getEventIfExist(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("no such event"));
    }
}
