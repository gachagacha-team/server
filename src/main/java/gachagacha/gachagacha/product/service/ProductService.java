package gachagacha.gachagacha.product.service;

import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.item.entity.ItemGrade;
import gachagacha.gachagacha.item.entity.UserItem;
import gachagacha.gachagacha.product.dto.*;
import gachagacha.gachagacha.product.entity.Product;
import gachagacha.gachagacha.product.entity.ProductStatus;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.item.repository.UserItemRepository;
import gachagacha.gachagacha.product.repository.ProductRepository;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserItemRepository userItemRepository;
    private final ProductRepository productRepository;

    public List<ReadAllProductsResponse> readAllProducts() {
        // 모든 등급의 아이템 리스트 찾기
        // 각 아이템 정보 반환(이미지 url, 수량 유무)
        return Arrays.stream(Item.values())
                .map(item -> {
                    List<Product> products = productRepository.findByItem(item);
                    return new ReadAllProductsResponse(item.getItemId(), "/image/items/" + item.getImageFileName(), !products.isEmpty());
                })
                .toList();
    }

    public List<ReadAllProductsResponse> readProductsByGrade(String grade) {
        // 특정 등급의 아이템 리스트 찾기
        ItemGrade itemGrade = ItemGrade.findByViewName(grade);
        List<Item> items = Item.getItemsByGrade(itemGrade);

        // 각 아이템 정보 반환(이미지 url, 수량 유무)
        return items.stream()
                .map(item -> {
                    List<Product> products = productRepository.findByItem(item);
                    return new ReadAllProductsResponse(item.getItemId(), "/image/items/" + item.getImageFileName(), !products.isEmpty());
                })
                .toList();
    }

    public ReadOneProductResponse readOneProduct(long itemId) {
        Item item = Item.findById(itemId);
        List<Product> products = productRepository.findByItem(item);
        return new ReadOneProductResponse(item.getViewName(), item.getItemGrade().getViewName(), products.size(), "/image/items/" + item.getImageFileName());
    }

    public Slice<ReadMyOneProductResponse> readMyProducts(Pageable pageable, HttpServletRequest request) {
        long userId = jwtUtils.getUserIdFromHeader(request);
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        return productRepository.findBySeller(seller, pageable)
                .map(product -> {
                    if (product.getProductStatus() == ProductStatus.ON_SALE) {
                        return ReadMyOneProductResponse.fromOnSale(product.getId(), product.getItem().getViewName(), product.getItem().getItemGrade().getViewName(),
                                "/image/items/" + product.getItem().getImageFileName(), product.getItem().getItemGrade().getProductPrice(), product.getProductStatus().getViewName());
                    } else {
                        return ReadMyOneProductResponse.fromSold(product.getId(), product.getItem().getViewName(), product.getItem().getItemGrade().getViewName(),
                                "/image/items/" + product.getItem().getImageFileName(), product.getItem().getItemGrade().getProductPrice(), product.getProductStatus().getViewName(), product.getTransactionDate());
                    }
                });
    }

    public Slice<ReadMyOneProductResponse> readMyProductsByGrade(String grade, Pageable pageable, HttpServletRequest request) {
        ItemGrade itemGrade = ItemGrade.findByViewName(grade);
        long userId = jwtUtils.getUserIdFromHeader(request);
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Slice<Product> products = productRepository.findBySeller(seller, pageable);
        List<ReadMyOneProductResponse> dtoList = products
                .filter(product -> product.getItem().getItemGrade() == itemGrade)
                .map(product -> {
                    if (product.getProductStatus() == ProductStatus.ON_SALE) {
                        return ReadMyOneProductResponse.fromOnSale(product.getId(), product.getItem().getViewName(), product.getItem().getItemGrade().getViewName(),
                                "/image/items/" + product.getItem().getImageFileName(), product.getItem().getItemGrade().getProductPrice(), product.getProductStatus().getViewName());
                    } else {
                        return ReadMyOneProductResponse.fromSold(product.getId(), product.getItem().getViewName(), product.getItem().getItemGrade().getViewName(),
                                "/image/items/" + product.getItem().getImageFileName(), product.getItem().getItemGrade().getProductPrice(), product.getProductStatus().getViewName(), product.getTransactionDate());
                    }
                })
                .toList();
        return new SliceImpl<>(dtoList, pageable, products.hasNext());
    }

    public ReadItemForSaleResponse readItemInfoForSale(long itemId) {
        Item item = Item.findById(itemId);
        return new ReadItemForSaleResponse(item.getItemId(), item.getViewName(), item.getItemGrade().getViewName(), item.getItemGrade().getProductPrice());
    }

    public void addProduct(AddProductRequest addProductRequest, HttpServletRequest request) {
        UserItem userItem = userItemRepository.findById(addProductRequest.getUserItemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ITEM));
        Item item = userItem.getItem();

        userItemRepository.delete(userItem);
        User seller = userRepository.findById(jwtUtils.getUserIdFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Product product = Product.create(seller, item);
        productRepository.save(product);
    }

    public void deleteProduct(long productId, HttpServletRequest request) {
        User seller = userRepository.findById(jwtUtils.getUserIdFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        if (product.getSeller().getId() != seller.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        productRepository.delete(product);
        UserItem userItem = UserItem.create(product.getItem());
        seller.cancelProduct(userItem);
    }

    public void buy(long itemId, HttpServletRequest request) {
        long userId = jwtUtils.getUserIdFromHeader(request);
        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Item item = Item.findById(itemId);

        // 수량 있는지 확인
        List<Product> products = productRepository.findByItemOrderByCreatedAtAsc(item);
        if (products.isEmpty()) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_PRODUCT);
        }

        // 구매자: 코인 차감, 아이템 지급 / 판매자: 코인 지급
        Product product = products.get(0); // 가장 처음에 등록된 상품이 판매됨
        product.processTrade(buyer);
        UserItem userItem = UserItem.create(product.getItem());
        buyer.addItem(userItem);
    }
}
