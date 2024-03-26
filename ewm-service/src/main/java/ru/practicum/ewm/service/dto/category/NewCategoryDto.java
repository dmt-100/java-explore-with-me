package ru.practicum.ewm.service.dto.category;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class NewCategoryDto {
    private Long id;
    @NotBlank
    @Size(max = 50)
    private String name;
}
