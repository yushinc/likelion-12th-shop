package com.likelion12th.shop.repository;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.entity.Review;
import com.likelion12th.shop.entity.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
public class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Test
    @DisplayName("댓글 테스트")
    public void CreateReviewTest(){
        Member member=new Member();
        Item item = new Item();
        Review review = new Review();
        review.setContent("Test Review Content");
        review.setMember(member);
        review.setItem(item);



        Review savedReivew=reviewRepository.save(review);
        System.out.println(savedReivew.toString());
    }
}
