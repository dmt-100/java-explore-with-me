package ru.practicum.ewm.service.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.service.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.service.dto.event.EventShortDto;
import ru.practicum.ewm.service.entity.Compilation;
import ru.practicum.ewm.service.entity.Event;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public CompilationDto toDto(Compilation compilation) {
        List<Event> events = compilation.getEvents();
        List<EventShortDto> shortEvents = EventMapper.toShortDtoList(events);
        return CompilationDto.builder()
                .events(shortEvents)
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public List<CompilationDto> toDtoList(List<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    public Compilation toEntity(NewCompilationDto dto) {
        return Compilation.builder()
                .pinned(dto.getPinned() != null ? dto.getPinned() : false)
                .title(dto.getTitle())
                .build();
    }
}
