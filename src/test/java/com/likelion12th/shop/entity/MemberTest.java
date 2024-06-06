package com.likelion12th.shop.entity;

import com.likelion12th.shop.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.test.context.support.WithMockUser;



@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@Transactional
class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username="seeun",roles="USER")
    //가짜 사용자로 테스트를 실행한다
    public void auditingTest(){
        Member newmember=new Member();
        memberRepository.save(newmember);

        em.flush();
        em.clear();

        Member member=memberRepository.findById(newmember.getId())
                        .orElseThrow(EntityNotFoundException::new);

        System.out.println("resister time: "+member.getRegTime());
        System.out.println("update time: "+member.getUpdateTime());
        System.out.println("creater: "+member.getCreatedBy());
        System.out.println("modifier: "+member.getModifiedBy());
    }

}

