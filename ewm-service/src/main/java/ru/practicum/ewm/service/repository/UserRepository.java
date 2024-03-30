package ru.practicum.ewm.service.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.service.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByIdInOrderById(List<Long> ids, PageRequest page);

    boolean existsByName(String name);
}
