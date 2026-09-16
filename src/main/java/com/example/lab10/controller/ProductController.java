package com.example.lab10.controller;

import com.example.lab10.model.Product;
import com.example.lab10.service.ProductService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ProductController — Reactive REST Controller
 *
 * ✅ @RestController, @RequestMapping, Constructor Injection ครบแล้ว
 * ✅ endpoint GET /products/{id} ทำเสร็จแล้วเป็นตัวอย่าง (30%)
 * ❌ TODO: เติม method body ของ endpoint ที่เหลือ (70%)
 *
 * Endpoints ที่ต้องทำทั้งหมด:
 * GET /products → Flux<Product> (ดึงทั้งหมด)
 * GET /products/{id} → Mono<Product> ✅ ตัวอย่างทำแล้ว
 * POST /products → Mono<Product> (บันทึก)
 * DELETE /products/{id} → Mono<Void> (ลบ)
 * GET /products/category/{cat} → Flux<Product> (กรอง)
 * GET /products/{id}/price → Mono<Double> (ราคาหลังลด)
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    // ── Constructor Injection (DIP — SOLID) ─────────────
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // GET /products/{id}
    // ค้นหา Product 1 รายการ
    @GetMapping("/{id}")
    public Mono<Product> getById(@PathVariable String id) {
        return service.getById(id);
    }

    // GET /products
    // ค้นหา Product ทั้งหมด
    @GetMapping
    public Flux<Product> getAll() {
        return service.getAll();
    }

    // POST /products
    // รับ Product จาก Request Body
    @PostMapping
    public Mono<Product> save(@RequestBody Product product) {
        return service.save(product);
    }

    // DELETE /products/{id}
    // ลบ Product
    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return service.delete(id);
    }

    // GET /products/category/{category}
    // ค้นหา Product ตาม Category
    @GetMapping("/category/{category}")
    public Flux<Product> getByCategory(@PathVariable String category) {
        return service.getByCategory(category);
    }

    // GET /products/{id}/price
    // คำนวณราคาหลังส่วนลด
    @GetMapping("/{id}/price")
    public Mono<Double> getDiscountedPrice(@PathVariable String id) {
        return service.getDiscountedPrice(id);
    }
}
