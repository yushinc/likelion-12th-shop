package com.likelion12th.shop.entity;

import com.likelion12th.shop.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.security.test.context.support.WithMockUser;
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@Transactional
public class MemberTest {
    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "mk", roles = "USER")
    public void auditingTest(){
        Member member1=new Member();
        member1=memberRepository.save(member1);

        em.flush();
        em.clear();

        Member member=memberRepository.findById(member1.getId())
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("register time : " + member.getRegTime());
        System.out.println("update time : " + member.getUpdateTime());
        System.out.println("creater : " + member.getCreatedBy());
        System.out.println("modifier : " + member.getModifiedBy());
    }
}
