/*package ru.practicum.ewm.service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationsController {
    CompilationsService compilationsService;
    @GetMapping
    public List<CompilationDto> getAllCompilations() {
        return compilationsService.getAllCompilations();
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationsById(@PathVariable long compId) {
        return compilationsService.getCompilationsById(compId);
    }
}*/
