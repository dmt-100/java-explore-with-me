package ru.practicum.ewm.service.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;

@UtilityClass
public class DefaultValues {
    public PageRequest createPage(int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}
