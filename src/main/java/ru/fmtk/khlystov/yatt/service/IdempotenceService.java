package ru.fmtk.khlystov.yatt.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.yatt.domain.IdempotenceUid;
import ru.fmtk.khlystov.yatt.exception.BadRequestException;
import ru.fmtk.khlystov.yatt.repository.IdempotenceUidRepository;

@Service
public class IdempotenceService {

    private final IdempotenceUidRepository idempotenceUidRepository;

    private final TimeService timeService;

    public IdempotenceService(IdempotenceUidRepository idempotenceUidRepository, TimeService timeService) {
        this.idempotenceUidRepository = idempotenceUidRepository;
        this.timeService = timeService;
    }

    @Transactional
    public Mono<Boolean> isExistsOrStore(@NonNull String idempotenceUid) {
        if (StringUtils.isBlank(idempotenceUid)) {
            return Mono.error(new BadRequestException("Idempotence uid doesn't have to be null"));
        }
        return idempotenceUidRepository.getByUid(idempotenceUid)
                .map(uid -> true)
                .switchIfEmpty(
                        idempotenceUidRepository.save(new IdempotenceUid(idempotenceUid, timeService.getDateTime()))
                                .map(uid -> false)
                );
    }
}
