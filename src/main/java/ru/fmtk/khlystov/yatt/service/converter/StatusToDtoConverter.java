package ru.fmtk.khlystov.yatt.service.converter;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.fmtk.khlystov.yatt.domain.Status;
import ru.fmtk.khlystov.yatt.dto.StatusDto;
import ru.fmtk.khlystov.yatt.repository.StatusRepository;

import static ru.fmtk.khlystov.yatt.dto.StatusDto.NULL_STATUS;

@Service
public class StatusToDtoConverter {

    private final StatusRepository statusRepository;

    public StatusToDtoConverter(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public Mono<StatusDto> toDto(Status status) {
        return Mono.just(new StatusDto(status.getId(), status.getName(), status.getDescription()));
    }

    public Mono<StatusDto> toDto(Long statusId) {
        if (statusId == null) {
            return Mono.just(NULL_STATUS);
        }
        return statusRepository.findById(statusId)
                .flatMap(this::toDto)
                .defaultIfEmpty(NULL_STATUS);
    }

    public Status toEntity(StatusDto statusDto) {
        return new Status(statusDto.getId(), statusDto.getName(), statusDto.getDescription());
    }
}
