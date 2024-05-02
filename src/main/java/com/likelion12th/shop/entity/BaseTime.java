package com.likelion12th.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

@EnableJpaAuditing
@EntityListeners(value ={AuditingEntityListener.class})
@MappedSuperclass // JPA Entity 클래스들이 BaseTimeEntity을 상속할 경우 필드들 (createdDate, modifiedDate)도 컬럼으로 인식하도록 한다!
@Getter @Setter
public abstract class BaseTime {

    @CreatedDate
    @Column(updatable = false) // 최초 저장 후 수정 금지
    private LocalDateTime regTime;

    @LastModifiedDate
    @Column(updatable = false)
    private LocalDateTime updateTime;
}