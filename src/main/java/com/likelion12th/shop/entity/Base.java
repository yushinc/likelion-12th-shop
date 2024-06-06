package com.likelion12th.shop.entity;

import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

public abstract class Base extends BaseTime {

    //등록자
    @CreatedBy
    @Column(updatable = false) //수정 불가
    private String createdBy;

    //수정자
    @LastModifiedBy
    private String modifiedBy;

}
