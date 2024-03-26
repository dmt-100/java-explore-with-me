package ru.practicum.stat.server.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String exception;
}
