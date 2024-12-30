package gachagacha.gachagacha.product.entity;

import gachagacha.gachagacha.BaseEntity;
import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @Enumerated(value = EnumType.STRING)
    private Item item;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus productStatus;

    private LocalDateTime transactionDate;

    public static Product create(User seller, Item item) {
        Product product = new Product();
        product.seller = seller;
        product.item = item;
        product.productStatus = ProductStatus.ON_SALE;
        return product;
    }

    public void processTrade(User buyer) {
        this.buyer = buyer;
        int productPrice = this.getItem().getItemGrade().getProductPrice();
        this.buyer.deductCoin(productPrice);
        seller.addCoin(productPrice);
        productStatus = ProductStatus.COMPLETED;
    }
}
