package ru.practicum.ewm.service.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.service.dto.event.enums.StateAction;
import ru.practicum.ewm.service.entity.Location;

import javax.validation.constraints.Size;

@Data
@Builder
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    String annotation;
    Long category;
    @Size(min = 20, max = 7000)
    String description;
    String eventDate;
    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    StateAction stateAction;
    @Size(min = 3, max = 120)
    String title;
}
