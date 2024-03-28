package ru.practicum.ewm.service.controller.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.user.NewUserRequest;
import ru.practicum.ewm.service.dto.user.UserDto;
import ru.practicum.ewm.service.service.admin.UserAdminService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin/users")
@Slf4j
public class UserAdminController {
    UserAdminService userAdminService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Get users {} {} {}", ids, from, size);
        return userAdminService.getUsers(ids == null ? Collections.emptyList() : ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Valid NewUserRequest userDto) {
        log.info("Add user {}", userDto);
        return userAdminService.addUser(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Delete user {}", userId);
        userAdminService.deleteUser(userId);
    }
}
