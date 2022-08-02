package ru.fmtk.khlystov.yatt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.fmtk.khlystov.yatt.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {UserRepository.class})
class YattApplicationTests {

    @Test
    void contextLoads() {
    }

}
