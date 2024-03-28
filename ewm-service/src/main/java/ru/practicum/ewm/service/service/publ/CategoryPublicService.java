package ru.practicum.ewm.service.service.publ;

import ru.practicum.ewm.service.dto.category.CategoryResponseDto;

import java.util.List;

public interface CategoryPublicService {
    List<CategoryResponseDto> getCategories(int from, int size);

    CategoryResponseDto getCategoryById(Long catId);
}
