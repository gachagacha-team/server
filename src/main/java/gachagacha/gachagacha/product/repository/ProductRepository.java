package gachagacha.gachagacha.product.repository;

import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.product.entity.Product;
import gachagacha.gachagacha.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByItem(Item item);
    List<Product> findByItemOrderByCreatedAtAsc(Item item);

    Slice<Product> findBySeller(User seller, Pageable pageable);
}
