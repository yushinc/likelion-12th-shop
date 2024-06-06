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

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
public class MemberTest {
    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "yerim", roles = "USER")
    public void auditingTest() {
        Member member = new Member();
        memberRepository.save(member);

        em.flush();
        em.clear();

        memberRepository.findById(member.getId()).orElseThrow(EntityNotFoundException::new);

        System.out.println("register time: " + member.getRegTime());
        System.out.println("update time: " + member.getUpdateTime());
        System.out.println("creater: " + member.getCreateBy());
        System.out.println("modifier: " + member.getModifiedBy());

    }

}
