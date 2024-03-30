package ru.practicum.ewm.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.service.dto.event.enums.State;
import ru.practicum.ewm.service.entity.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAll(Specification<Event> specification, Pageable page);

    List<Event> findAllByInitiatorIdOrderByCreatedOn(Long userId, PageRequest page);

    boolean existsByCategoryId(Long catId);

    List<Event> findAllByStateIsOrderByCreatedOn(State state);

    List<Event> findAllByInitiatorIdAndStateIs(Long userId, State state);

}
