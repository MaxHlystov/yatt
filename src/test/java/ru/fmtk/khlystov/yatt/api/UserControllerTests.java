package ru.fmtk.khlystov.yatt.api;

import java.time.Duration;

import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.fmtk.khlystov.yatt.dto.UserDto;
import ru.fmtk.khlystov.yatt.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
//@WebFluxTest(controllers = UserController.class)
//@Import(UserRepository.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                UserRepository.class,
                UserController.class
        }
//        properties = "spring.liquibase.enabled=false" // liquibase конфигурируем вручную
)
//@TestPropertySource(locations = "classpath:functional-test.yaml")
@ActiveProfiles("unittest")
//@AutoConfigureMockMvc
public class UserControllerTests {

    static {
        System.setProperty("logging.config", "classpath:log4j2-test.xml");
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    protected UserRepository userRepository;

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            log.info("Starting test: " + description.getClassName() + " " + description.getMethodName());
        }
    };

    @Test
    public void getUser_ReturnsUsers() {
//        var users = userRepository.findAll();
//        StepVerifier.create(users)
//                .expectNextCount(5)
//                .verifyComplete();
    }

    @Test
    void dataTest() {
        var dataLimit = 10;
        var timeOut = 20;
        var result = webTestClient
                .get().uri("/api/v1/user")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(UserDto.class)
                .getResponseBody()
                .take(dataLimit)
                .timeout(Duration.ofSeconds(timeOut))
                .collectList()
                .block();
        assertThat(result).hasSize(3);
    }
}
