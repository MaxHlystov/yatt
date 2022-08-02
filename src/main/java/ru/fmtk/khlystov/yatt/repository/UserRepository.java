package ru.fmtk.khlystov.yatt.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.yatt.domain.User;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findUserByName(@Param("name") String name);
}
