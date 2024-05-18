package com.likelion12th.shop.entity;
import com.likelion12th.shop.dto.ReviewFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter @Setter @ToString
public class Review {
    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public static Review createReview(ReviewFormDto reviewFormDto){
        Review review=new Review();
        review.setContent(reviewFormDto.getContent());
        review.item.getItemName();
        review.member.getName();
        return review;
    }
}
