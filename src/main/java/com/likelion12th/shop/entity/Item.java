package com.likelion12th.shop.entity;


import com.likelion12th.shop.constant.SellStatus;
import com.likelion12th.shop.repository.ItemRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.antlr.v4.runtime.misc.LogManager;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter @Setter @ToString
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private Integer price;
    private Integer stock;
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private SellStatus itemSellStatus;
    private LocalDateTime createdBy;
    private LocalDateTime modifiedBy;

    private String itemImg;
    private String itemImgPath;



}
