package ru.practicum.ewm.service.service.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.controller.advice.exception.NotFoundException;
import ru.practicum.ewm.service.controller.advice.exception.ValidationException;
import ru.practicum.ewm.service.dto.user.NewUserRequest;
import ru.practicum.ewm.service.dto.user.UserDto;
import ru.practicum.ewm.service.entity.User;
import ru.practicum.ewm.service.repository.UserRepository;
import ru.practicum.ewm.service.util.DefaultValues;
import ru.practicum.ewm.service.util.mapper.UserMapper;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {
    UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        if (!ids.isEmpty()) {
            return UserMapper.toDtoList(
                    userRepository.findAllByIdInOrderById(ids, DefaultValues.createPage(from, size)));
        }
        return UserMapper.toDtoList(
                userRepository.findAll(DefaultValues.createPage(from, size)).getContent());
    }

    @Override
    @Transactional
    public UserDto addUser(NewUserRequest userDto) {
        if (userRepository.existsByName(userDto.getName())) {
            throw new ValidationException("username already exist");
        }
        User user = UserMapper.toUser(userDto);
        return UserMapper.toDto(
                userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("no such user"));
        userRepository.deleteById(userId);
    }
}
