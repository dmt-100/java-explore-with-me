package ru.practicum.ewm.service.service.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.controller.advice.exception.NotFoundException;
import ru.practicum.ewm.service.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.service.dto.compilation.UpdateCompilationDto;
import ru.practicum.ewm.service.entity.Compilation;
import ru.practicum.ewm.service.entity.Event;
import ru.practicum.ewm.service.repository.CompilationRepository;
import ru.practicum.ewm.service.repository.EventRepository;
import ru.practicum.ewm.service.util.mapper.CompilationMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class CompilationsAdminService {
    CompilationRepository compilationRepository;
    EventRepository eventRepository;

    public CompilationDto addCompilation(NewCompilationDto dto) {
        List<Event> events = getEventList(dto.getEvents());
        Compilation compilation = CompilationMapper.toEntity(dto);
        compilation.setEvents(events);
        return CompilationMapper.toDto(
                compilationRepository.save(compilation));
    }

    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    public CompilationDto updateCompilation(Long compId, UpdateCompilationDto dto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("no such compilation"));
        if (dto.getPinned() != null)
            compilation.setPinned(dto.getPinned());
        if (dto.getTitle() != null)
            compilation.setTitle(dto.getTitle());
        if (dto.getEvents() != null)
            compilation.setEvents(getEventList(dto.getEvents()));
        return CompilationMapper.toDto(compilation);
    }

    private List<Event> getEventList(Set<Long> eventIds) {
        if (eventIds == null) {
            return Collections.emptyList();
        }
        List<Event> events = new ArrayList<>();
        eventIds.forEach(id -> {
            Event event = eventRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("no such event"));
            events.add(event);
        });
        return events;
    }
}
