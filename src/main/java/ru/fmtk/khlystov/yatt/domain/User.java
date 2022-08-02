package ru.fmtk.khlystov.yatt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "user", schema = "yatt")
public class User {

    public static final User NULL_USER = new User(-1L, "NULL", null);

    @Id
    private Long id;

    @NonNull
    private String name;

    private Long telegramId;
}
