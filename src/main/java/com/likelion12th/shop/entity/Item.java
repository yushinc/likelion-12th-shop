package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
@Setter
@ToString
public class Item extends BaseTime {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;

    private Integer price;

    private Integer stock;

    private String itemDetail;

    private String itemImg;

    private String itemImgPath;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;


}
