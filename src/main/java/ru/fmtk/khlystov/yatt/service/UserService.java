package ru.fmtk.khlystov.yatt.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.yatt.domain.User;
import ru.fmtk.khlystov.yatt.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> findOrCreateUserByTelegramId(long telegramId, String userName) {
        return userRepository.findUserByTelegramId(telegramId)
                .switchIfEmpty(userRepository.findUserByName(userName)
                        .flatMap(user -> {
                            user.setTelegramId(telegramId);
                            return userRepository.save(user);
                        }))
                .switchIfEmpty(userRepository.save(new User(null, userName, telegramId)));
    }
}
