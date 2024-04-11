package com.likelion12th.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import com.likelion12th.shop.constant.itemSellStatus;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Getter
public class Item {
    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private Integer price;
    private Integer stock;
    private String itemDetail;


    @Enumerated(EnumType.STRING)
    private itemSellStatus itemsellstatus;

    @OneToMany(mappedBy = "item")
    private List<CartItem> cartItems;




    private LocalDateTime createBy;
    private LocalDateTime modifiedBy;
}
