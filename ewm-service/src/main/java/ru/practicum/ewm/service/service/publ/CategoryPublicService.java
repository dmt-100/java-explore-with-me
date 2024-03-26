package ru.practicum.ewm.service.service.publ;

import ru.practicum.ewm.service.dto.category.CategoryDto;

import java.util.List;

public interface CategoryPublicService {
    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(Long catId);
}
