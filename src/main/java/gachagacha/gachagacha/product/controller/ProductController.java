package gachagacha.gachagacha.product.controller;

import gachagacha.gachagacha.product.dto.*;
import gachagacha.gachagacha.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/market")
    public List<ReadAllProductsResponse> readAllProducts(@RequestParam(value = "grade", required = false) String grade) {
        if (grade != null) {
            return productService.readProductsByGrade(grade);
        } else {
            return productService.readAllProducts();
        }
    }

    @GetMapping("/market/items/{itemId}")
    public ReadOneProductResponse readOneProduct(@PathVariable long itemId) {
        return productService.readOneProduct(itemId);
    }

    @GetMapping("/market/myProducts")
    public Slice<ReadMyOneProductResponse> readMyProducts(@RequestParam(value = "grade", required = false) String grade, Pageable pageable, HttpServletRequest request) {
        if (grade != null) {
            return productService.readMyProductsByGrade(grade, pageable, request);
        } else {
            return productService.readMyProducts(pageable, request);
        }
    }

    @GetMapping("/market/sale/{itemId}")
    public ReadItemForSaleResponse readItemInfoForSale(@PathVariable long itemId) {
        return productService.readItemInfoForSale(itemId);
    }

    @PostMapping("/market/sale")
    public void addProduct(@RequestBody AddProductRequest addProductRequest, HttpServletRequest request) {
        productService.addProduct(addProductRequest, request);
    }

    @DeleteMapping("/market/{productId}")
    public void deleteProduct(@PathVariable long productId, HttpServletRequest request) {
        productService.deleteProduct(productId, request);
    }

    @PostMapping("/market/buy/{itemId}")
    public void buy(@PathVariable long itemId, HttpServletRequest request) {
        productService.buy(itemId, request);
    }
}
