package ru.practicum.ewm.service.service.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.dto.user.NewUserRequest;
import ru.practicum.ewm.service.dto.user.UserDto;
import ru.practicum.ewm.service.entity.User;
import ru.practicum.ewm.service.controller.advice.exception.NotFoundException;
import ru.practicum.ewm.service.util.DefaultValues;
import ru.practicum.ewm.service.util.mapper.UserMapper;
import ru.practicum.ewm.service.repository.UserRepository;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {
    UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Integer> userIds, int from, int size) {
        return UserMapper.toDtoList(
                userRepository.getUsers(userIds, DefaultValues.createPage(from, size)));
    }

    @Override
    public UserDto addUser(NewUserRequest userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toDto(
                userRepository.save(user));
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User doesn't exist");
        }
        userRepository.deleteById(userId);
    }
}
