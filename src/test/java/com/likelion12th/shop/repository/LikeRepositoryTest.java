package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Like;
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

public class LikeRepositoryTest {
    @Autowired
    LikeRepository likeRepository;

    @Autowired
    MemberRepository memberRepository;

    public Member createMember(){
        Member member = new Member();
        member.setName("김세은");
        member.setEmail("rlatpdms0104@naver.com");
        member.setPassword("qwerty1234");
        member.setAddress("세종시");

        Member savedMember=memberRepository.save(member);
        return savedMember;
    }

//    @Test
//    @DisplayName("찜 테스트")
//    public void createLikeTest(){
//        Member member = createMember();
//        Like like = new Like();
//        like.setId("rlatpdms0104");
//        like.setItem("사과");
//        like.setPrice("3000원");
//        like.setItem("사과");
//        like.setMember(member);
//
//        Like savedlike=likeRepository.save(like);
//        System.out.println(savedlike.toString());
//    }


}

