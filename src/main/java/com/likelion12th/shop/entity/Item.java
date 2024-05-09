package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.ItemSellStatus;
import com.likelion12th.shop.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table
@Getter @Setter
@ToString  //로그 생성안될 시, 추가하기!!
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String itemName;
    private Integer price;
    private Integer stock;
    private String itemDetail;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    private String itemImg;
    private String itemImgPath;

    @Enumerated(EnumType.STRING)  //enum으로 설정한 코드
    private ItemSellStatus itemSellStatus;


}
