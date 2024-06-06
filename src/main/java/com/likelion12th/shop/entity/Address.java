package com.likelion12th.shop.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class Address {

    private String country;
    private String city;
    private String street;

}
