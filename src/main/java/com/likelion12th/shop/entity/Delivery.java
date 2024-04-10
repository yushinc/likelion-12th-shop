package com.likelion12th.shop.entity;

import com.likelion12th.shop.Dto.DeliveryFormDto;
import com.likelion12th.shop.Dto.MemberFormDto;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.web.WebProperties;


@Entity
@Table
@Getter @Setter
@ToString
public class Delivery {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer deliveryAmount;
    private Integer deliveryPrice;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Embedded
    private Address address;

}
