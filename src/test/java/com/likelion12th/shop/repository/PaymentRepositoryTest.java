package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Payment;
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
class PaymentRepositoryTest {

    @Autowired
    PaymentRepository paymentRepository;

    @Test
    @DisplayName("결제 테스트")
    public void createPaymentTest() {
        Payment payment = new Payment();
        payment.setAmount(2);
        payment.setPrice(25000);
        payment.setPaymentMethod("creditcard");
        payment.setCustomerId("seohee");

        Payment savedPayment = paymentRepository.save(payment);
        System.out.println(savedPayment.toString());
    }

}