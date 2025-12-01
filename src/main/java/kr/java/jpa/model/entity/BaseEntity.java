package kr.java.jpa.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // <- 테이블 X.
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 활성화
@Getter // Setter X
public abstract class BaseEntity extends BaseTimeEntity {
    // 최초작성자, 최초생성자
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy; // userInfo PK
    // 최종수정자
    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;
}
