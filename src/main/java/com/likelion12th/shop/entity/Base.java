package com.likelion12th.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value={AuditingEntityListener.class})
@MappedSuperclass
@Getter
public abstract class Base extends BaseTime{
    // 생성자
    @CreatedBy
    @Column(updatable = false) // 수정불가
    private String createdBy;


    // 수정
    @LastModifiedBy
    private String modifiedBy;
}
