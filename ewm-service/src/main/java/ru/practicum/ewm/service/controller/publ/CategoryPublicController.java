package ru.practicum.ewm.service.controller.publ;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.category.CategoryResponseDto;
import ru.practicum.ewm.service.service.publ.CategoryPublicService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/categories")
@Slf4j
public class CategoryPublicController {
    CategoryPublicService categoryPublicService;

    @GetMapping
    public List<CategoryResponseDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        log.info("Get categories, from, size {} {}", from, size);
        return categoryPublicService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryResponseDto getCategoryById(@PathVariable Long catId) {
        log.info("Get category by id {}", catId);
        return categoryPublicService.getCategoryById(catId);
    }
}
