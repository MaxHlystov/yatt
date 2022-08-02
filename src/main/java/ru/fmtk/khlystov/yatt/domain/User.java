package ru.fmtk.khlystov.yatt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

@AllArgsConstructor
@Getter
@ToString
@Table(name = "user", schema = "yatt")
public class User {
    @Id
    private Long id;

    @NonNull
    private String name;
}
