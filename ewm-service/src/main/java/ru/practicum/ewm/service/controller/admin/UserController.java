package ru.practicum.ewm.service.controller.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.user.UserDto;
import ru.practicum.ewm.service.service.admin.UserServiceAdmin;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin/users")
public class UserController {
    UserServiceAdmin userServiceAdmin;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> userIds,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        return userServiceAdmin.getUsers(userIds, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {
        return userServiceAdmin.addUser(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userServiceAdmin.deleteUser(userId);
    }
}
