package ru.fmtk.khlystov.yatt.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.fmtk.khlystov.yatt.dto.StatusDto;
import ru.fmtk.khlystov.yatt.repository.StatusRepository;
import ru.fmtk.khlystov.yatt.service.converter.StatusToDtoConverter;

@RestController
@RequestMapping("/api")
public class StatusController {

    private final StatusRepository statusRepository;
    private final StatusToDtoConverter statusToDtoConverter;

    public StatusController(StatusRepository statusRepository, StatusToDtoConverter statusToDtoConverter) {
        this.statusRepository = statusRepository;
        this.statusToDtoConverter = statusToDtoConverter;
    }

    @Operation(description = "Get all statuses",
            responses = {@ApiResponse(responseCode = "200", description = "get all statuses")})
    @GetMapping("/v1/status")
    public Flux<StatusDto> getAll() {
        return statusRepository.findAll()
                .map(statusToDtoConverter::toDto);
    }
}
