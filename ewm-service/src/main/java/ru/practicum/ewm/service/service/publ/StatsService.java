package ru.practicum.ewm.service.service.publ;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.entity.Event;
import ru.practicum.stat.client.StatClient;
import ru.practicum.stat.dto.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.util.DefaultValues.DATETIME_PATTERN;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatsService {
    private static final String APP_NAME = "ewm-main-service";
    StatClient statClient;
    ObjectMapper mapper;

    public Map<Long, Long> getViews(List<Event> events) {
        if (events.isEmpty()) {
            return Collections.emptyMap();
        }
        LocalDateTime startDateForAll = events.stream()
                .map(Event::getPublishedOn)
                .min(LocalDateTime::compareTo)
                .get();

        List<Long> ids = events.stream().map(Event::getId).collect(Collectors.toList());
        List<String> uris = ids.stream().map(id -> String.format("/events/%d", id)).collect(Collectors.toList());

        ResponseEntity<Object> response = statClient.getStats(
                startDateForAll,
                LocalDateTime.now(),
                uris, true);

        Map<Long, Long> resultMap = new HashMap<>();
        if (response.hasBody() && response.getStatusCode().is2xxSuccessful()) {
            List<ViewStatsDto> resultList = mapper.convertValue(response.getBody(), new TypeReference<>() {
            });
            for (ViewStatsDto dto : resultList) {
                String index = dto.getUri().substring(8);
                resultMap.put(Long.parseLong(index), (dto.getHits()));
            }
        }
        return resultMap;
    }

    public void addHit(HttpServletRequest request) {
        statClient.addHit(APP_NAME,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATETIME_PATTERN))
        );
    }

    private List<ViewStatsDto> convertToStats(ResponseEntity<Object> response) {
        if (response.hasBody() && response.getStatusCode().is2xxSuccessful()) {
            return mapper.convertValue(response.getBody(), new TypeReference<>() {
            });
        }
        return Collections.emptyList();
    }
}
