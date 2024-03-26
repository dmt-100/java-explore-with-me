package ru.practicum.ewm.service.service.publ;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.controller.advice.exception.NotFoundException;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.event.enums.AvailableValues;
import ru.practicum.ewm.service.entity.Event;
import ru.practicum.ewm.service.repository.EventRepository;
import ru.practicum.ewm.service.util.DefaultValues;
import ru.practicum.ewm.service.util.mapper.EventMapper;
import ru.practicum.stat.client.StatClient;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventPublicService {
    EventRepository eventRepository;
    static StatClient statClient;

    public List<EventFullDto> getEvents(String text,
                                        List<Integer> categories,
                                        Boolean paid,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Boolean onlyAvailable,
                                        AvailableValues sort,
                                        Integer from,
                                        Integer size) {
        List<Event> events = eventRepository.findEventsSortedByEventDate(
                        text,categories,paid,rangeStart, rangeEnd, onlyAvailable, DefaultValues.createPage(from, size));
        List<EventFullDto> dtoList = EventMapper.toDtoList(events);
        //TODO getViews from StatClient
        if (sort.equals(AvailableValues.VIEWS)) {
            return dtoList.stream()
                    .sorted(Comparator.comparing(EventFullDto::getViews, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        }
        return dtoList;
    }

    public EventFullDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("no such event"));
        return EventMapper.toDto(event);
    }
}
