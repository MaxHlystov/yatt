package ru.fmtk.khlystov.yatt.repository;

import java.util.Set;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.yatt.domain.Task;

public interface TaskRepository extends ReactiveCrudRepository<Task, Long> {
    @Query("select t.* from yatt.task as t " +
            "  inner join yatt.user as u on u.id = t.assignee_id " +
            "where u.name = :assigneeName " +
            "  and t.status_id in :statusIds")
    Flux<Task> findAllByAssigneeNameAndStatusIdIn(@Param("assigneeName") String assigneeName,
                                                    @Param("statusIds") Set<Long> statusIds);

    @Query("select t.* from yatt.task as t " +
            "  inner join yatt.user as u on u.id = t.assignee_id " +
            "where u.name = :assigneeName")
    Flux<Task> findAllByAssigneeName(@Param("assigneeName") String assigneeName);

    Flux<Task> findAllByStatusIdIn(Set<Long> statusIds);

    @Modifying
    @Query("update yatt.task set status_id = :statusId where id = :taskId")
    Mono<Task> updateStatus(@Param("taskId") long taskId, @Param("statusId") long statusId);

    @Modifying
    @Query("update yatt.task set assignee_id = :assigneeId where id = :taskId")
    Mono<Task> setAssignee(@Param("taskId") long taskId, @Param("assigneeId") Long assigneeId);
}
