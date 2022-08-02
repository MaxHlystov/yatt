package ru.fmtk.khlystov.yatt.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class TaskDto {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSSSSS";

    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private StatusDto status;

    private String description;

    private UserDto assignee;

    @JsonFormat(pattern=DATE_PATTERN)
    private LocalDateTime createdAt;

    private UserDto createdBy;

    @JsonFormat(pattern=DATE_PATTERN)
    private LocalDateTime modifiedAt;

    private UserDto modifiedBy;

}
