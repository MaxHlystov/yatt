package ru.fmtk.khlystov.yatt.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.fmtk.khlystov.yatt.dto.ErrorDto;
import ru.fmtk.khlystov.yatt.exception.BadRequestException;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler({
            BadRequestException.class,
    })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDto badRequest(Exception e) {
        log.error(e.getMessage(), e);
        return new ErrorDto(e.getMessage());
    }
}
