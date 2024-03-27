package ru.practicum.ewm.service.util;

import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import ru.practicum.ewm.service.controller.advice.exception.BadRequestException;
import ru.practicum.ewm.service.dto.event.enums.State;
import ru.practicum.ewm.service.entity.Event;

import javax.persistence.criteria.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@UtilityClass
public class DefaultValues {
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public PageRequest createPage(int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }

    public static String[] getNullPropertyNames(Object source) {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(source.getClass());
        return Stream.of(propertyDescriptors)
                .map(PropertyDescriptor::getName)
                .filter(propertyName -> {
                    Method getter = ReflectionUtils.findMethod(source.getClass(), "get" + StringUtils.capitalize(propertyName));
                    return getter != null && ReflectionUtils.invokeMethod(getter, source) == null;
                })
                .toArray(String[]::new);
    }

    public static Specification<Event> createEventSpecification(String text, List<Integer> categories,
                                                                Boolean paid, LocalDateTime rangeStart,
                                                                LocalDateTime rangeEnd, Boolean onlyAvailable) {
        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
            rangeEnd = rangeStart.plusYears(1);
        }
        if (rangeStart != null && rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("range end must be later than range start");
        }
        LocalDateTime finalRangeStart = rangeStart;
        LocalDateTime finalRangeEnd = rangeEnd;

        Specification<Event> specification = (Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("state"), State.PUBLISHED));
            if (text != null)
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("description")), "%" + text.toLowerCase() + "%")
                ));
            if (categories != null) {
                predicates.add(root.join("category", JoinType.INNER).get("id").in(categories));
            }
            if (paid != null)
                predicates.add(cb.equal(root.get("paid"), paid));
            predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), finalRangeStart));
            predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), finalRangeEnd));
            if (onlyAvailable != null && onlyAvailable)
                predicates.add(cb.greaterThan(root.get("participantLimit"), root.get("confirmedRequests")));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return specification;
    }

    public static Specification<Event> createAdminSpecification(List<Long> users, State states,
                                                                List<Long> categories, LocalDateTime rangeStart,
                                                                LocalDateTime rangeEnd) {
        Specification<Event> specification = (Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (users != null && !users.isEmpty())
                predicates.add(root.get("initiator").in(users));
            if (states != null)
                predicates.add(cb.equal(root.get("state"), states));
            if (categories != null && !categories.isEmpty())
                predicates.add(root.join("category", JoinType.INNER).get("id").in(categories));
            if (rangeStart != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
            if (rangeEnd != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return specification;
    }
}
