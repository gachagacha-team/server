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

    @GetMapping("/items/{itemId}/forSale")
    public ReadItemForSaleResponse readItemInfoForSale(@PathVariable long itemId) {
        return productService.readItemInfoForSale(itemId);
    }

    @PostMapping("/products")
    public void addProduct(@RequestBody AddProductRequest addProductRequest, HttpServletRequest request) {
        productService.addProduct(addProductRequest, request);
    }

    @GetMapping("/products")
    public List<ReadAllProductsResponse> readAllProducts(@RequestParam(value = "grade", required = false) String grade) {
        return productService.readProducts(grade);
    }

    @GetMapping("/products/me")
    public Slice<ReadMyOneProductResponse> readMyProducts(@RequestParam(value = "grade", required = false) String grade, Pageable pageable, HttpServletRequest request) {
        return productService.readMyProducts(grade, pageable, request);
    }

    @GetMapping("/items/{itemId}/products")
    public ReadOneProductResponse readOneProduct(@PathVariable long itemId) {
        return productService.readOneProduct(itemId);
    }

    @DeleteMapping("/products/{productId}")
    public void deleteProduct(@PathVariable long productId, HttpServletRequest request) {
        productService.deleteProduct(productId, request);
    }

    @PostMapping("/items/{itemId}/purchase")
    public void purchase(@PathVariable long itemId, HttpServletRequest request) {
        productService.purchase(itemId, request);
    }
}
