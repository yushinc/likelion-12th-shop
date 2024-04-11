package com.likelion12th.shop.repository;

import com.likelion12th.shop.constant.DeliveryStatus;
import com.likelion12th.shop.entity.Delivery;
import com.likelion12th.shop.entity.Item;
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

class DeliveryRepositoryTest {
    @Autowired
    DeliveryRepository deliveryRepository;

    @Test
    @DisplayName("배송건 생성 테스트")
    public void createDeliveryTest() {
        Delivery delivery = new Delivery();
        delivery.setWaybillNumber("12345");
        delivery.setDeliveryStatus(DeliveryStatus.valueOf("RESERVATION"));

        Delivery savedDelivery = deliveryRepository.save(delivery);
        System.out.println(savedDelivery.toString());
    }
}