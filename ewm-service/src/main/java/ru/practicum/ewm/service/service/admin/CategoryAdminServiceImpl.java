package ru.practicum.ewm.service.service.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.controller.advice.exception.NotFoundException;
import ru.practicum.ewm.service.controller.advice.exception.ValidationException;
import ru.practicum.ewm.service.dto.category.CategoryRequestDto;
import ru.practicum.ewm.service.dto.category.CategoryResponseDto;
import ru.practicum.ewm.service.entity.Category;
import ru.practicum.ewm.service.repository.CategoryRepository;
import ru.practicum.ewm.service.repository.EventRepository;
import ru.practicum.ewm.service.util.mapper.CategoryMapper;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryAdminServiceImpl implements CategoryAdminService {
    CategoryRepository categoryRepository;
    EventRepository eventRepository;

    @Override
    public CategoryResponseDto addCategory(CategoryRequestDto dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new ValidationException("Category name already exist");
        }
        return CategoryMapper.toDto(
                categoryRepository.save(
                        CategoryMapper.toEntity(dto)));
    }

    @Override
    public void deleteCategory(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("no such category");
        }
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ValidationException("category has pinned events");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryResponseDto patchCategory(Long catId, CategoryRequestDto dto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("no such category"));
        category.setName(dto.getName());
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.toDto(savedCategory);
    }
}
