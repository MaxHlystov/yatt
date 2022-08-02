package ru.fmtk.khlystov.yatt.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.fmtk.khlystov.yatt.domain.Status;

public interface StatusRepository extends ReactiveCrudRepository<Status, Long> {
}
