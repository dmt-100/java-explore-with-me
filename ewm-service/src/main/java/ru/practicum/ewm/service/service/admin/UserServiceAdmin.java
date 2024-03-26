package ru.practicum.ewm.service.service.admin;

import ru.practicum.ewm.service.dto.user.UserDto;

import java.util.List;

public interface UserServiceAdmin {
    List<UserDto> getUsers(List<Integer> userIds, int from, int size);

    UserDto addUser(UserDto userDto);

    void deleteUser(Long userId);
}
