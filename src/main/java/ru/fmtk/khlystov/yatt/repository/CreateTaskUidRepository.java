package ru.fmtk.khlystov.yatt.repository;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.yatt.domain.CreateTaskUid;

public interface CreateTaskUidRepository extends ReactiveCrudRepository<CreateTaskUid, String> {

    @Modifying
    @Query("insert into yatt.create_task_uid (uid, created_at)" +
            "  values (:uid, :createdAt)" +
            "  on conflict (uid) do nothing" +
            "  returning *")
    Mono<CreateTaskUid> insertIfNotExists(
            @Param("uid") @NonNull String uid,
            @Param("createdAt") @NonNull LocalDateTime createdAt);
}
