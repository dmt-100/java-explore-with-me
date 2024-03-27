package ru.practicum.ewm.service.dto.category;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class CategoryRequestDto {
    private Long id;
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
