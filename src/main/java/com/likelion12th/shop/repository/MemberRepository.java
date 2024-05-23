package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.entity.Order;
import jakarta.persistence.Column;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByEmail(String email);
}
