package ru.practicum.ewm.service.controller.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.category.CategoryDto;
import ru.practicum.ewm.service.dto.category.NewCategoryDto;
import ru.practicum.ewm.service.service.admin.CategoryAdminService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    CategoryAdminService categoryAdminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Valid NewCategoryDto dto) {
        return categoryAdminService.addCategory(dto);
    }

    /*@SneakyThrows
    @GetMapping(value = "/easter-egg", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> easterEgg() {
        ClassPathResource img = new ClassPathResource("images/oh-no-5c0886.jpg");
        byte[] bytes = StreamUtils.copyToByteArray(img.getInputStream());
        return ResponseEntity.ok().body(bytes);
    }*/

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        categoryAdminService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto patchCategory(@PathVariable Long catId,
                                     @RequestBody @Valid NewCategoryDto dto) {
        return categoryAdminService.patchCategory(catId, dto);
    }
}
