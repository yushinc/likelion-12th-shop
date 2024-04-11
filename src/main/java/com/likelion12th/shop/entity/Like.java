package com.likelion12th.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@ToString
public class Like {
    //
    @Id
    @Column(name="like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;


    private String itemName;
    private Integer price;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

//    public static like createlike(MemberLikeDte memberLikeDte){
//        like like= new like();
//        like.setItem(MemberLikeDte.getItem());
//        like.setPrice(MemberLikeDte.getPrice());
//        like.setLikeitem(MemberLikeDte.getLikeitem());
//
//
//        return like;
//    }


}
