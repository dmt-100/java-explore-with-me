package ru.practicum.ewm.service.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.service.dto.participation.ParticipationResponseDto;
import ru.practicum.ewm.service.entity.Request;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public ParticipationResponseDto toDto(Request request) {
        return ParticipationResponseDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public List<ParticipationResponseDto> toDtoList(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
