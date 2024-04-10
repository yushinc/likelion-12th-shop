package com.likelion12th.shop.entity;


import com.likelion12th.shop.constant.likeitem;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
public class like {
    //
    @Id
    @Column(name="like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    private String itemName;
    private Integer price;

    @Enumerated(EnumType.STRING)
    private likeitem likeitem;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;


}
