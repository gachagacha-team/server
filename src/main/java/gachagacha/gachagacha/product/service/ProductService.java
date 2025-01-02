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

    public List<ReadAllProductsResponse> readProducts(String grade) {
        List<Item> items = (grade == null) ? Arrays.asList(Item.values()) : Item.getItemsByGrade(ItemGrade.findByViewName(grade));
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

    public Slice<ReadMyOneProductResponse> readMyProducts(String grade, Pageable pageable, HttpServletRequest request) {
        User seller = userRepository.findById(jwtUtils.getUserIdFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        Slice<Product> productSlice = productRepository.findBySeller(seller, pageable);
        List<Product> products = productSlice.getContent();

        if (grade != null) {
            products = filterByGrade(products, ItemGrade.findByViewName(grade));
        }

        List<ReadMyOneProductResponse> ReadMyOneProductResponses = products.stream()
                .map(product -> {
                    ReadMyOneProductResponse readMyOneProductResponse = new ReadMyOneProductResponse(product.getId(),
                            "/image/items/" + product.getItem().getImageFileName(),
                            product.getItem().getViewName(),
                            product.getItem().getItemGrade().getViewName(),
                            product.getItem().getItemGrade().getProductPrice(),
                            product.getProductStatus().getViewName()
                    );
                    if (product.getProductStatus() == ProductStatus.COMPLETED) {
                        readMyOneProductResponse.setTransactionDate(product.getTransactionDate());
                    }
                    return readMyOneProductResponse;
                })
                .toList();

        return new SliceImpl<>(ReadMyOneProductResponses, pageable, productSlice.hasNext());
    }

    private List<Product> filterByGrade(List<Product> products, ItemGrade itemGrade) {
        return products.stream()
                .filter(product -> product.getItem().getItemGrade() == itemGrade)
                .toList();
    }

    public ReadItemForSaleResponse readItemInfoForSale(long itemId) {
        Item item = Item.findById(itemId);
        return new ReadItemForSaleResponse(item.getItemId(), item.getViewName(), item.getItemGrade().getViewName(), item.getItemGrade().getProductPrice());
    }

    public void addProduct(AddProductRequest addProductRequest, HttpServletRequest request) {
        UserItem userItem = userItemRepository.findById(addProductRequest.getUserItemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ITEM));
        User seller = userRepository.findById(jwtUtils.getUserIdFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        seller.saleUserItem(userItem);
        productRepository.save(Product.create(seller, userItem.getItem()));
        userItemRepository.delete(userItem);
    }

    public void deleteProduct(long productId, HttpServletRequest request) {
        User seller = userRepository.findById(jwtUtils.getUserIdFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        validateDeleteProductAuthorization(product, seller);
        seller.cancelProductAndRevertToUserItem(product.getItem());
        productRepository.delete(product);
    }

    private void validateDeleteProductAuthorization(Product product, User seller) {
        if (product.getSeller().getId() != seller.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }

    public void purchase(long itemId, HttpServletRequest request) {
        User buyer = userRepository.findById(jwtUtils.getUserIdFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        Item item = Item.findById(itemId);

        Product product = findProduct(item);
        product.processTrade(buyer);
    }

    private Product findProduct(Item item) {
        List<Product> products = productRepository.findByItemOrderByCreatedAtAsc(item);
        if (products.isEmpty()) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_PRODUCT);
        }
        return products.get(0);
    }
}
