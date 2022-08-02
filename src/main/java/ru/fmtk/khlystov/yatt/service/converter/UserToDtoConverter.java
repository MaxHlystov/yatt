package ru.fmtk.khlystov.yatt.service.converter;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.yatt.domain.User;
import ru.fmtk.khlystov.yatt.dto.UserDto;
import ru.fmtk.khlystov.yatt.repository.UserRepository;

import static ru.fmtk.khlystov.yatt.dto.UserDto.NULL_USER;

@Service
public class UserToDtoConverter {

    private final UserRepository userRepository;

    public UserToDtoConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<UserDto> toDto(User user) {
        return Mono.just(UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build());
    }

    public Mono<UserDto> toDto(Long userId) {
        if (userId == null) {
            return Mono.just(NULL_USER);
        }
        return userRepository.findById(userId)
                .flatMap(this::toDto)
                .defaultIfEmpty(NULL_USER);
    }

    public User toEntity(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName());
    }
}
