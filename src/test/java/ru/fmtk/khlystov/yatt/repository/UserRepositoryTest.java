package ru.fmtk.khlystov.yatt.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.fmtk.khlystov.yatt.config.RepositoryInContainerTest;
import ru.fmtk.khlystov.yatt.domain.User;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataR2dbcTest
public class UserRepositoryTest extends RepositoryInContainerTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testUserRepositoryExists() {
        assertNotNull(userRepository);
    }

    @Test
    public void testFindAll() {
        Flux<User> users = userRepository.findAll();

        StepVerifier.create(users.log())
                .expectNextCount(3)
                .verifyComplete();

    }

    @Test
    public void testFindById() {
        Mono<User> user1 = userRepository.findById(1L);
        StepVerifier.create(user1.log())
                .expectNextMatches(user -> user.getId() == 1L)
                .verifyComplete();
    }

    @Test
    public void testCreateUser() {
        Mono<User> saved = userRepository.save(new User(null, "test", null));
        StepVerifier.create(saved.log())
                .expectNextMatches(user -> user.getId() == 5L &&
                        "test".equals(user.getName()) &&
                        user.getTelegramId() == null
                ).verifyComplete();

        userRepository.deleteById(4L).block();
    }

    @Test
    public void testCreateUserWithTelegramId() {
        Mono<User> saved = userRepository.save(new User(null, "telegram user", 1L));
        StepVerifier.create(saved.log())
                .expectNextMatches(user ->
                        user.getId() == 4L &&
                                "telegram user".equals(user.getName()) &&
                                user.getTelegramId() == 1L
                )
                .verifyComplete();

        userRepository.deleteById(4L).block();
    }
}
