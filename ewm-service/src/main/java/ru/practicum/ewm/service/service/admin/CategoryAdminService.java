package ru.practicum.ewm.service.service.admin;

import ru.practicum.ewm.service.dto.category.CategoryRequestDto;
import ru.practicum.ewm.service.dto.category.CategoryResponseDto;

public interface CategoryAdminService {
    CategoryResponseDto addCategory(CategoryRequestDto dto);

    void deleteCategory(Long catId);

    CategoryResponseDto patchCategory(Long catId, CategoryRequestDto dto);
}
