package ru.fmtk.khlystov.yatt.service.converter;

import org.springframework.stereotype.Service;
import ru.fmtk.khlystov.yatt.domain.Status;
import ru.fmtk.khlystov.yatt.dto.StatusDto;

@Service
public class StatusToDtoConverter {
    public StatusDto toDto(Status status) {
        return new StatusDto(status.getId(), status.getName());
    }

    public Status toEntity(StatusDto statusDto) {
        return new Status(statusDto.getId(), statusDto.getName());
    }
}
