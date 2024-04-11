package com.likelion12th.shop.dto;

import com.likelion12th.shop.constant.ReviewStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewFormDto {
    private ReviewStatus reviewStatus;
    private Float star;
    private String content;
    private String imgPath;
    private Long itemId;
    private Long memberId;
}
