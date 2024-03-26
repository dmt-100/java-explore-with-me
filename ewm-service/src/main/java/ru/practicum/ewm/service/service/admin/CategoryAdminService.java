package ru.practicum.ewm.service.service.admin;

import ru.practicum.ewm.service.dto.category.CategoryDto;
import ru.practicum.ewm.service.dto.category.NewCategoryDto;

public interface CategoryAdminService {
    CategoryDto addCategory(NewCategoryDto dto);

    void deleteCategory(Long catId);

    CategoryDto patchCategory(Long catId, NewCategoryDto dto);
}
