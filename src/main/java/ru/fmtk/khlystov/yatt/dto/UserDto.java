package ru.fmtk.khlystov.yatt.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserDto {

    public static final UserDto NULL_USER = new UserDto(-1L, "NULL");

    private Long id;

    @NotEmpty
    private String name;
}
