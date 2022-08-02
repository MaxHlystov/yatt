package ru.fmtk.khlystov.yatt.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserDto {
    private Long id;

    @NotEmpty
    private String name;
}
