package ru.practicum.ewm.service.service.publ;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.controller.advice.exception.NotFoundException;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.event.enums.Sorted;
import ru.practicum.ewm.service.dto.event.enums.State;
import ru.practicum.ewm.service.entity.Event;
import ru.practicum.ewm.service.repository.EventRepository;
import ru.practicum.ewm.service.util.DefaultValues;
import ru.practicum.ewm.service.util.mapper.EventMapper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.dto.event.enums.Sorted.VIEWS;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventPublicService {
    EventRepository eventRepository;
    StatsService statsService;

    public List<EventFullDto> getEvents(String text,
                                        List<Integer> categories,
                                        Boolean paid,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Boolean onlyAvailable,
                                        Sorted sort,
                                        Integer from,
                                        Integer size,
                                        HttpServletRequest request) {
        statsService.addHit(request);
        PageRequest page = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "eventDate"));
        Specification<Event> specification = DefaultValues.createEventSpecification(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable
        );
        List<Event> events = eventRepository.findAll(specification, page).getContent();
        List<EventFullDto> dtoList = EventMapper.toDtoList(events);
        Map<Long, Long> views = statsService.getViews(events);
        dtoList.forEach(eventDto ->
                eventDto.setViews(views.getOrDefault(eventDto.getId(), 0L)));
        if (sort != null && sort.equals(VIEWS)) {
            return dtoList.stream().sorted(Comparator.comparing(EventFullDto::getViews)).collect(Collectors.toList());
        }
        return dtoList;
    }

    public EventFullDto getEventById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("no such event"));
        statsService.addHit(request);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("event not published");
        }
        EventFullDto dto = EventMapper.toDto(event);
        Map<Long, Long> views = statsService.getViews(List.of(event));
        dto.setViews(views.getOrDefault(event.getId(), 0L));
        return dto;
    }
}
