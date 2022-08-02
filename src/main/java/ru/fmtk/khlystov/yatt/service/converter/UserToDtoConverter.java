package ru.fmtk.khlystov.yatt.service.converter;

import org.springframework.stereotype.Service;
import ru.fmtk.khlystov.yatt.domain.User;
import ru.fmtk.khlystov.yatt.dto.UserDto;

@Service
public class UserToDtoConverter {

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public User toEntity(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName());
    }
}
