package kr.java.jpa.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // <- 테이블 X.
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 활성화
@Getter // Setter X
public abstract class BaseTimeEntity {

    // 생성일시 -> PrePersist, CURRENT_TIMESTAMP, 시간객체.now()...
    @CreatedDate
    @Column(name = "created_at", updatable = false) // 수정 X
    private LocalDateTime createdAt; // getter로 불러들이고...

    // 수정일시 - 수정될 때마다 업데이트
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
