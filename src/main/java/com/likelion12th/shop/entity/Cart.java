package com.likelion12th.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
public class Cart {
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdBY;
    private LocalDateTime modifiedBY;

    @OneToOne
    @JoinColumn(name="member_id")
    private Member member;
}
