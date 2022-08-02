package ru.fmtk.khlystov.yatt.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class StatusDto {
    private Long id;

    @NotEmpty
    private String name;
}
