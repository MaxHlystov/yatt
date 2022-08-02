package ru.fmtk.khlystov.yatt.domain;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@ToString
@Table(name = "idempotence_uid", schema = "yatt")
public class IdempotenceUid implements Persistable<String> {

    @Transient
    private boolean isNew = true;

    @NotEmpty
    @Id
    private String uid;

    @NotNull
    private final LocalDateTime createdAt;

    public IdempotenceUid(String uid, LocalDateTime createdAt) {
        this.uid = uid;
        this.createdAt = createdAt;
    }

    @Override
    public String getId() {
        return uid;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void markNotNew() {
        this.isNew = false;
    }
}
