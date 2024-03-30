package ru.practicum.ewm.service.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.service.entity.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("SELECT c FROM Compilation as c " +
            "WHERE (:pinned) IS NULL OR c.pinned = (:pinned)")
    List<Compilation> findAllByPinned(Boolean pinned, PageRequest page);
}
