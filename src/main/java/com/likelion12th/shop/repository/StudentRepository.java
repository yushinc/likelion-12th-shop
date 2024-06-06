package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository <Student, Long>{
}
