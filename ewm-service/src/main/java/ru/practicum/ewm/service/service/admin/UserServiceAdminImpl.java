package ru.practicum.ewm.service.service.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.dto.user.UserDto;
import ru.practicum.ewm.service.entity.User;
import ru.practicum.ewm.service.exception.NotFoundException;
import ru.practicum.ewm.service.mapper.UserMapper;
import ru.practicum.ewm.service.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceAdminImpl implements UserServiceAdmin {
    UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Integer> userIds, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return userRepository.getUsers(userIds, page).stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toDto(
                userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User doesn't exist");
        }
        userRepository.deleteById(userId);
    }
}
