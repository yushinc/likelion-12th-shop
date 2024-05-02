package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.Role;
import com.likelion12th.shop.dto.MemberFormDto;
import com.likelion12th.shop.dto.StudentFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="student")
@Getter
@Setter
@ToString
public class Student {

    @Id
    @Column(name="student_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique=true)
    private String Number;

    public static Student createstudent(StudentFormDto studentFormDto){
        Student student= new Student();
        student.setName(studentFormDto.getName());
        student.setNumber(studentFormDto.getNumber());


        return student;
    }
}

