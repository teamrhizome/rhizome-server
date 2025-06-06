package org.rhizome.server.support;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    public LocalDateTime getCreatedAt() {
        return createdAt.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    public LocalDateTime getDeletedAt() {
        if (deletedAt == null) {
            return null;
        }
        return deletedAt.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
