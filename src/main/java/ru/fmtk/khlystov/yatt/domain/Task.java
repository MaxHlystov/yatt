package ru.fmtk.khlystov.yatt.domain;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

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

    public static final Task NULL_TASK = Task.builder()
            .id(-1L)
            .name("NULL")
            .statusId(-1L)
            .build();

    @Id
    private Long id;

    @NotEmpty
    private String name;

    private long statusId;

    private String description;

    private Long assigneeId;

    private LocalDateTime createdAt;

    private Long createdBy;

    private LocalDateTime modifiedAt;

    private Long modifiedBy;
}
