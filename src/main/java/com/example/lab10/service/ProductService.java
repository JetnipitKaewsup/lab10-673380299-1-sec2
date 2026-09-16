package com.example.lab10.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.lab10.model.Product;
import com.example.lab10.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ProductService — Business Logic Layer
 *
 * ✅ @Service, Constructor Injection ครบแล้ว (DIP — SOLID)
 * ❌ TODO: เติม method body ให้ครบทุก method
 *
 * หน้าที่: รับ request จาก Controller → เรียก Repository → คืนผล
 * (SRP — แต่ละ class มีหน้าที่เดียว)
 *
 * Hint Operators ที่ควรใช้:
 * .map(p -> ...) แปลงค่า
 * .flatMap(p -> ...) async transform
 * .defaultIfEmpty(...) fallback ถ้าว่าง
 * .switchIfEmpty(Mono...) fallback Mono ถ้าว่าง
 */
@Service
public class ProductService {

    // ── Constructor Injection (DIP — SOLID) ─────────────
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    // ── 1. ดึง Product 1 รายการ ──────────────────────────
    public Mono<Product> getById(String id) {
        return repository.findById(id) // ค้นหาจาก id
                // ถ้า Repository ไม่พบข้อมูล
                // ให้เปลี่ยนจาก Mono.empty() เป็น Error
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found: " + id)));
    }

    // ── 2. ดึง Product ทั้งหมด ───────────────────────────
    public Flux<Product> getAll() {
        return repository.findAll();
    }

    // ── 3. บันทึก Product ────────────────────────────────
    public Mono<Product> save(Product product) {
        // ถ้า Product ยังไม่มี id ให้สร้าง id ใหม่
        if (product.getId() == null) {
            product.setId(UUID.randomUUID().toString());
        }
        // ส่ง Product ให้ Repository บันทึก
        return repository.save(product);
    }

    // ── 4. ลบ Product ────────────────────────────────────
    public Mono<Void> delete(String id) {
        return repository.deleteById(id); // ลบ Product ตาม id
    }

    // ── 5. กรองตาม category ──────────────────────────────
    public Flux<Product> getByCategory(String category) {
        return repository.findByCategory(category); // ค้นหา Product ตาม Category
    }

    // ── 6. คำนวณราคาหลังส่วนลด ───────────────────────────
    public Mono<Double> getDiscountedPrice(String id) {
        return getById(id) // getById() ได้ Mono<Product>
                .map(p -> p.getDiscountedPrice()); // แปลง Product เป็นราคาหลังส่วนลด
    }
}
