package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class DeliveryRepositoryTest {
    @Autowired
    DeliveryRepository deliveryRepository;

    @Test
    @DisplayName("배송 테스트")
    public void CreateDeliveryTest(){
        Delivery delivery = new Delivery();
        delivery.setDeliveryAmount(4);
        delivery.setDeliveryPrice(12500);
        delivery.setDeliveryStatus(DeliveryStatus.READY);

        Delivery savedDelivery = deliveryRepository.save(delivery);
        System.out.println(savedDelivery.toString());
    }
}
