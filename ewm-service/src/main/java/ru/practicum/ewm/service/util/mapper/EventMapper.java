package ru.practicum.ewm.service.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.entity.Event;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public EventFullDto toDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(formatter))
                .paid(event.getPaid())
                .location(event.getLocation())
                .initiator(UserMapper.toShortUser(event.getInitiator()))
                .confirmedRequests(event.getConfirmedRequests())
                .state(event.getState())
                .title(event.getTitle())
                .publishedOn(event.getPublishedOn().format(formatter))
                .createdOn(event.getCreatedOn().format(formatter))
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .category(CategoryMapper.toDto(event.getCategory()))
                .annotation(event.getAnnotation())
                .build();
    }

    public List<EventFullDto> toDtoList(List<Event> events) {
        return events.stream()
                .map(EventMapper::toDto)
                .collect(Collectors.toList());
    }
}
