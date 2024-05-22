package com.likelion12th.shop.entity;

import com.likelion12th.shop.constant.ItemSellStatus;
import com.likelion12th.shop.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    // 재고 삭제
    public void removeStock(int stock) {
        int restStock = this.stock - stock;
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족 합니다.(현재 재고 수량: " + this.stock + ")");
        }
        this.stock = restStock;
    }

    public void addStock(int stock) {this.stock += stock;}
}
