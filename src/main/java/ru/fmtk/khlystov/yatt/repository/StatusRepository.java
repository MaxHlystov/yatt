package ru.fmtk.khlystov.yatt.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.yatt.domain.Status;

public interface StatusRepository extends ReactiveCrudRepository<Status, Long> {
    Mono<Status> findByNameOrDescription(@Param("name") String name,
                                         @Param("description") String description);
}
