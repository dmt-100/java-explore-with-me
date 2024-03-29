package ru.practicum.ewm.service.service.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.controller.advice.exception.BadRequestException;
import ru.practicum.ewm.service.controller.advice.exception.NotFoundException;
import ru.practicum.ewm.service.controller.advice.exception.ValidationException;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.service.dto.event.enums.State;
import ru.practicum.ewm.service.dto.event.enums.StateAction;
import ru.practicum.ewm.service.entity.Event;
import ru.practicum.ewm.service.repository.EventRepository;
import ru.practicum.ewm.service.service.publ.StatsService;
import ru.practicum.ewm.service.util.mapper.EventMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static ru.practicum.ewm.service.service.priv.EventPrivateService.isEventDateCorrect;
import static ru.practicum.ewm.service.util.DefaultValues.*;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventAdminService {
    EventRepository eventRepository;
    StatsService statsService;

    @Transactional(readOnly = true)
    public List<EventFullDto> searchEvents(List<Long> users,
                                           State states,
                                           List<Long> categories,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           Integer from,
                                           Integer size) {
        Specification<Event> specification = createAdminSpecification(users, states, categories, rangeStart, rangeEnd);
        PageRequest page = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findAll(specification, page).getContent();
        List<EventFullDto> dtoList = EventMapper.toDtoList(events);
        Map<Long, Long> views = statsService.getViews(events);
        dtoList.forEach(eventDto ->
                eventDto.setViews(views.getOrDefault(eventDto.getId(), 0L)));
        return dtoList;
    }

    @Transactional
    public EventFullDto confirmOrRejectEvent(Long eventId, UpdateEventAdminRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("no such event"));
        if (request.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(request.getEventDate(), DateTimeFormatter.ofPattern(DATETIME_PATTERN));
            if (eventDate.isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Event date must be later than current time");
            }
            if (!isEventDateCorrect(eventDate, 1)) {
                throw new ValidationException("Event date must be later for hour than publication date");
            }
        }
        if (event.getState() != State.PENDING) {
            throw new ValidationException("State must be PENDING");
        }
        BeanUtils.copyProperties(request, event, getNullPropertyNames(request));
        if (request.getStateAction() != null && request.getStateAction() == StateAction.PUBLISH_EVENT) {
            event.setState(State.PUBLISHED);
            return EventMapper.toDto(event);
        }
        event.setState(State.CANCELED);
        return EventMapper.toDto(event);
    }
}
