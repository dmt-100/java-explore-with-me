package ru.practicum.ewm.service.service.publ;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.controller.advice.exception.NotFoundException;
import ru.practicum.ewm.service.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.repository.CompilationRepository;
import ru.practicum.ewm.service.util.DefaultValues;
import ru.practicum.ewm.service.util.mapper.CompilationMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class CompilationPublicService {
    CompilationRepository compilationRepository;

    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        return CompilationMapper.toDtoList(
                compilationRepository.findAllByPinned(pinned, DefaultValues.createPage(from, size)));
    }

    public CompilationDto getCompilationsById(long compId) {
        return CompilationMapper.toDto(
                compilationRepository.findById(compId)
                        .orElseThrow(() -> new NotFoundException("no such compilation")));
    }
}
