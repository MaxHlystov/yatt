package ru.fmtk.khlystov.yatt.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TaskFilterDto {
    private Set<Long> statuses;
    private String assignee;

}
