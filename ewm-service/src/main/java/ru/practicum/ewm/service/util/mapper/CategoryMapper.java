package ru.practicum.ewm.service.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.service.dto.category.CategoryRequestDto;
import ru.practicum.ewm.service.dto.category.CategoryResponseDto;
import ru.practicum.ewm.service.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryMapper {
    public Category toEntity(CategoryRequestDto dto) {
        return Category.builder()
                .name(dto.getName())
                .build();
    }

    public CategoryResponseDto toDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public List<CategoryResponseDto> toDtoList(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
