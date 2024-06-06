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

    //등록자
    @CreatedBy
    @Column(updatable=false)//수정 불가
    private String createdBy;

    //수정자
    @LastModifiedBy
    //@Column(nullable = false, updatable = false)
    private String modifiedBy;
}
