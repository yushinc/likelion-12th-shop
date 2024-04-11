package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.ReviewStatus;
import com.likelion12th.shop.dto.ReviewFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter
@Setter
@ToString
public class Review {

    @Id
    @Column(name = "review_id")
    // ?QUA: nullable = false 설정하지 않아도 밑의 어노테이션 때문에 nullable false 되는 것인가요?
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    private Float star;

    private String content;

    private String imgPath;

    private LocalDateTime createdBy;

    private LocalDateTime modifiedBy;

    // ?QUA: item entity 나, member entity 에 대한 것의 정의는 id만 넣어주면 되는데 이는 어떻게 진행하면 될지 궁금합니다.
    public static Review createReview(ReviewFormDto reviewFormDto){
        Review review = new Review();
        review.setReviewStatus(reviewFormDto.getReviewStatus());
        review.setStar(reviewFormDto.getStar());
        review.setContent(reviewFormDto.getContent());
        review.setImgPath(reviewFormDto.getImgPath());

        return review;
    }

}
