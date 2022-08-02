package ru.fmtk.khlystov.yatt.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.yatt.domain.User;
import ru.fmtk.khlystov.yatt.dto.UserDto;
import ru.fmtk.khlystov.yatt.exception.BadRequestException;
import ru.fmtk.khlystov.yatt.repository.UserRepository;
import ru.fmtk.khlystov.yatt.service.converter.UserToDtoConverter;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;
    private final UserToDtoConverter userToDtoConverter;

    public UserController(UserRepository userRepository, UserToDtoConverter userToDtoConverter) {
        this.userRepository = userRepository;
        this.userToDtoConverter = userToDtoConverter;
    }

    @Operation(description = "Get all users",
            responses = {@ApiResponse(responseCode = "200", description = "get all users")})
    @GetMapping(value = "/v1/user", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<UserDto> getUsers() {
        return userRepository.findAll()
                .flatMap(userToDtoConverter::toDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get user by id"),
            @ApiResponse(responseCode = "404", description = "user not found")
    })
    @GetMapping(value = "/v1/user/{id}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<UserDto> getUserById(@PathVariable(name = "id") Long userId) {
        return userRepository.findById(userId)
                .flatMap(userToDtoConverter::toDto);
    }

    @Operation(description = "Create user",
            responses = {@ApiResponse(responseCode = "200", description = "create user")})
    @PostMapping(value = "/v1/user", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<Long> createUser(@RequestBody UserDto userDto) {
        if (StringUtils.isBlank(userDto.getName())) {
            return Mono.error(new BadRequestException("User name has to be specified."));
        }
        return userRepository.findUserByName(userDto.getName())
                .switchIfEmpty(userRepository.save(userToDtoConverter.toEntity(userDto)))
                .map(User::getId);
    }

    @Operation(description = "Change user",
            responses = {@ApiResponse(responseCode = "200", description = "change user")})
    @PostMapping("/v1/user/{userId}")
    public Mono<UserDto> changeUser(
            @PathVariable("userId") long userId,
            @RequestBody UserDto userDto
    ) {
        return userRepository.findById(userId)
                .flatMap(user -> userRepository.save(new User(userId, userDto.getName(), null)))
                .flatMap(userToDtoConverter::toDto);
    }

    @Operation(description = "Delete user",
            responses = {@ApiResponse(responseCode = "200", description = "delete user")})
    @DeleteMapping("/v1/user/{userId}")
    public Mono<Void> deleteUser(@PathVariable("userId") long userId) {
        return userRepository.deleteById(userId).then();
    }
}
