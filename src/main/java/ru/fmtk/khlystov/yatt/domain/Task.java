package ru.fmtk.khlystov.yatt.domain;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Table(name = "task", schema = "yatt")
public class Task {
    @Id
    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private Status status;

    private String description;

    private User assignee;

    private LocalDateTime createdAt;

    private User createdBy;

    private LocalDateTime modifiedAt;

    private User modifiedBy;
}
