package ru.practicum.ewm.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.service.entity.Category;



public interface CategoryRepository extends JpaRepository<Category, Long> {
}
