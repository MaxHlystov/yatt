package ru.fmtk.khlystov.yatt.domain;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Getter
@ToString
@Table(name = "create_task_uid", schema = "yatt")
public class CreateTaskUid {
    @NotEmpty
    @Id
    private String uid;

    @NotNull
    private LocalDateTime createdAt;
}
