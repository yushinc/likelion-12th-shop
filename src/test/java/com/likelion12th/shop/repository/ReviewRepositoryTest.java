package com.likelion12th.shop.repository;

import com.likelion12th.shop.constant.ReviewStatus;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.entity.Review;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Test
    @DisplayName("리뷰 테스트")
    public void createReivewTest(){
        Review review = new Review();
        review.setReviewStatus(ReviewStatus.AFTER_PURCHASE);
        review.setStar(3.f);
        review.setContent("This is test review");
        review.setImgPath("https://dimg.donga.com/wps/NEWS/IMAGE/2022/01/28/111500268.2.jpg");

        // 임시
        Member member = new Member();
        review.setMember(member);

        // 임시
        Item item = new Item();
        review.setItem(item);

        Review savedReview = reviewRepository.save(review);
        System.out.println(savedReview.toString());
    }

}