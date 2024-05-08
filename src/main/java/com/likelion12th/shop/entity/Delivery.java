package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.DeliveryStatus;
import com.likelion12th.shop.dto.DeliveryDto;
import com.likelion12th.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString
public class Delivery {
    @Id
    @Column(name = "delivery_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // order와 1:1 맵핑
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // member와 N:1 맵핑
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // 배송 상태
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    // 운송장 번호
    private String waybillNumber;

    // 생성 시간, 수정 시간 저장
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    // 배달 생성
    public static Delivery createDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = new Delivery();
        delivery.setMember(deliveryDto.getMember());
        delivery.setWaybillNumber(deliveryDto.getWaybillNumber());
        return delivery;
    }

}
