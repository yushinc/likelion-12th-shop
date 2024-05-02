package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Member;
import com.likelion12th.shop.entity.Student;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Test
    @DisplayName("학생 정보")
    public void createStudnetTest(){
        Student student = new Student();
        student.setName("김세은");
        student.setNumber("2023111373");

        Student savedStudent=studentRepository.save(student);
        System.out.println(savedStudent.toString());
    }

}


