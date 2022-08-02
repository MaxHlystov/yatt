package ru.fmtk.khlystov.yatt.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.yatt.domain.IdempotenceUid;

public interface IdempotenceUidRepository extends ReactiveCrudRepository<IdempotenceUid, String> {
    Mono<IdempotenceUid> getByUid(@NonNull String uid);
}
