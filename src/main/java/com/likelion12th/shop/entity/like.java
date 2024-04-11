package com.likelion12th.shop.entity;


import com.likelion12th.shop.constant.likeitem;
import com.likelion12th.shop.dto.MemberFormDto;
import com.likelion12th.shop.dto.MemberLikeDte;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@ToString
public class like {
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

    @Enumerated(EnumType.STRING)
    private likeitem likeitem;

    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    public static like createlike(MemberLikeDte memberLikeDte){
        like like= new like();
        like.setId(MemberLikeDte.getId());
        like.setItem(MemberLikeDte.getItem());
        like.setPrice(MemberLikeDte.getPrice());
        like.setLikeitem(MemberLikeDte.getLikeitem());


        return like;
    }


}
