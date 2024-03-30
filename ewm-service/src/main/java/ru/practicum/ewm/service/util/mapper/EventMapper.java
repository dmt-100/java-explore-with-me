package ru.practicum.ewm.service.util.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.event.EventShortDto;
import ru.practicum.ewm.service.dto.event.NewEventDto;
import ru.practicum.ewm.service.entity.Category;
import ru.practicum.ewm.service.entity.Event;
import ru.practicum.ewm.service.entity.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.util.DefaultValues.DATETIME_PATTERN;

@UtilityClass
@Slf4j
public class EventMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    public EventFullDto toDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(formatter))
                .paid(event.getPaid())
                .location(event.getLocation())
                .initiator(UserMapper.toShortUser(event.getInitiator()))
                .confirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() : 0)
                .state(event.getState())
                .title(event.getTitle())
                .publishedOn(event.getPublishedOn().format(formatter))
                .createdOn(event.getCreatedOn().format(formatter))
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .category(CategoryMapper.toDto(event.getCategory()))
                .annotation(event.getAnnotation())
                .adminComment(event.getAdminComment())
                .build();
    }

    public List<EventFullDto> toDtoList(List<Event> events) {
        return events.stream()
                .map(EventMapper::toDto)
                .collect(Collectors.toList());
    }

    public Event toEntity(NewEventDto dto, User initiator, Category category) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .eventDate(LocalDateTime.parse(dto.getEventDate(), formatter))
                .location(dto.getLocation())
                .paid(dto.getPaid() != null ? dto.getPaid() : false)
                .participantLimit(dto.getParticipantLimit() != null ? dto.getParticipantLimit() : 0)
                .publishedOn(LocalDateTime.now())
                .requestModeration(dto.getRequestModeration() != null ? dto.getRequestModeration() : true)
                .title(dto.getTitle())
                .initiator(initiator)
                .category(category)
                .confirmedRequests(0)
                .build();
    }

    public EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toShortUser(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public List<EventShortDto> toShortDtoList(List<Event> events) {
        return events.stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }
}
