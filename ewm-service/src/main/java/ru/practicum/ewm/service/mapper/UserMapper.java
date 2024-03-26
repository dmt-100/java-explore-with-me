package ru.practicum.ewm.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.service.dto.user.UserDto;
import ru.practicum.ewm.service.entity.User;

@UtilityClass
public class UserMapper {
    public User toUser(UserDto dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
