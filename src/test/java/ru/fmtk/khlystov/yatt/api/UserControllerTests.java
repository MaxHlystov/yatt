package ru.fmtk.khlystov.yatt.api;

import java.time.Duration;

import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.fmtk.khlystov.yatt.domain.User;
import ru.fmtk.khlystov.yatt.dto.UserDto;
import ru.fmtk.khlystov.yatt.repository.UserRepository;
import ru.fmtk.khlystov.yatt.service.converter.UserToDtoConverter;

import static org.mockito.Mockito.when;

@Slf4j
@WebFluxTest(controllers = {UserController.class})
@Import({UserToDtoConverter.class})
public class UserControllerTests {

    private static final int DATA_LIMIT = 10;
    private static final Duration TIME_OUT = Duration.ofSeconds(20);

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            log.info("Starting test: " + description.getClassName() + " " + description.getMethodName());
        }
    };

    @Test
    void testGetUser_ReturnsUsers() {
        when(userRepository.findAll())
                .thenReturn(Flux.just(
                        new User(1L, "user 1", null),
                        new User(2L, "telegramUser100", 100L),
                        new User(3L, "user 3", null)
                ));

        Flux<UserDto> result = webTestClient
                .get().uri("/api/v1/user")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(UserDto.class)
                .getResponseBody()
                .take(DATA_LIMIT)
                .timeout(TIME_OUT);
        StepVerifier.create(result.log())
                .expectNextMatches(user -> user.getId() == 1L &&
                        "user 1".equals(user.getName()))
                .expectNextMatches(user -> user.getId() == 2L &&
                        "telegramUser100".equals(user.getName()))
                .expectNextMatches(user -> user.getId() == 3L &&
                        "user 3".equals(user.getName()))
                .verifyComplete();
    }
}
