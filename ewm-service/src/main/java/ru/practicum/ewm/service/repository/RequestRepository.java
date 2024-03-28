package ru.practicum.ewm.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.service.entity.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByEventId(Long eventId);

    boolean existsByEventIdAndRequesterId(Long eventId, Long requesterId);

    List<Request> findAllByRequesterId(Long userId);
}
