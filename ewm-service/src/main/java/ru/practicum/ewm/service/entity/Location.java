package ru.practicum.ewm.service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {
    @Column(name = "lat")
    Float lat;
    @Column(name = "lon")
    Float lon;
}
