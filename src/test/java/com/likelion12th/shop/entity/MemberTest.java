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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "jung", roles = "USER")
    public void auditingTest() {
        Member member = new Member();
        member = memberRepository.save(member);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("resister time: " + findMember.getRegTime());
        System.out.println("update time: " + findMember.getUpdateTime());
        System.out.println("creater: " + findMember.getCreatedBy());
        System.out.println("modifier: " + findMember.getModifiedBy());
    }
}
