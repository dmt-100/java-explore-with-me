package ru.practicum.ewm.service.service.admin;

import ru.practicum.ewm.service.dto.user.NewUserRequest;
import ru.practicum.ewm.service.dto.user.UserDto;

import java.util.List;

public interface UserAdminService {
    List<UserDto> getUsers(List<Integer> userIds, int from, int size);

    UserDto addUser(NewUserRequest userDto);

    void deleteUser(Long userId);
}
