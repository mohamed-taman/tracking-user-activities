package com.siriusxi.tua.api.controller;


import com.siriusxi.tua.api.model.request.Product;
import com.siriusxi.tua.domain.ProductMessage;
import com.siriusxi.tua.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        log.info("[ProductController]: add new product = " + product.toString());
        this.productService.sendMessage(new ProductMessage(product, "add"));
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/products/{id}")
    void deleteProduct(@PathVariable String id) {
        log.info("[ProductController]: delete product id = " + id);
        this.productService.sendMessage(new ProductMessage(new Product(id), "delete"));
    }
}
