package ru.practicum.ewm.service.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.service.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query( "SELECT u " +
            "FROM User AS u " +
            "WHERE ((:userIds) IS NULL OR u.id IN :userIds)" +
            "ORDER BY u.id")
    List<User> getUsers(List<Integer> userIds, PageRequest page);
}
