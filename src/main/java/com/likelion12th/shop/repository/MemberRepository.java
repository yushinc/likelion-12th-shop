package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {//엔티티, pk 자료형
    Member findByEmail(String email);
}
