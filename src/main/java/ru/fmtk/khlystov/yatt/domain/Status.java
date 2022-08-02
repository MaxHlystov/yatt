package ru.fmtk.khlystov.yatt.domain;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Getter
@ToString
@Table(name = "status", schema = "yatt")
public class Status {
    @Id
    private Long id;

    @NotEmpty
    private String name;
}
