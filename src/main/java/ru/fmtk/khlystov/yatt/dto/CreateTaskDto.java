package ru.fmtk.khlystov.yatt.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateTaskDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String creatorName;

    private String description;
}
