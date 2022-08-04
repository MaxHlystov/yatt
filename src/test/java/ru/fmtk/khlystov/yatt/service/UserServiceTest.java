package ru.fmtk.khlystov.yatt.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.fmtk.khlystov.yatt.domain.User;
import ru.fmtk.khlystov.yatt.repository.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles({"test"})
@RunWith(SpringRunner.class)
@WebFluxTest(value = {UserService.class})
public class UserServiceTest {

    @MockBean
    public UserRepository userRepository;

    @Autowired
    public UserService userService;

    @Test
    public void testFindOrCreateUserByTelegramId() {
        final User baseUser = new User(1L, "telegramUser100", 100L);
        when(userRepository.findUserByName(anyString()))
                .thenReturn(Mono.just(baseUser));
        when(userRepository.findUserByTelegramId(anyLong()))
                .thenReturn(Mono.empty());
        when(userRepository.save(any())).thenReturn(Mono.just(baseUser));

        Mono<User> userMono = userService.findOrCreateUserByTelegramId(100L, "telegramUser100");

        StepVerifier.create(userMono.log())
                .expectNextMatches(user -> user.getTelegramId() == 100L)
                .verifyComplete();
    }
}
