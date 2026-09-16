package com.example.lab10.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.lab10.model.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ProductRepository — In-memory Reactive Repository
 *
 * ✅ โครงสร้างและ annotation ครบแล้ว
 * ❌ TODO: เติม method body ให้ครบทุก method
 *
 * ใช้ ConcurrentHashMap เป็น in-memory storage
 * (ไม่ต่อ Database — เน้นฝึก Mono/Flux)
 *
 * Hint:
 * - Mono.just(value) คืนค่าเดียว
 * - Mono.empty() คืนเปล่า
 * - Flux.fromIterable(list) คืนหลายค่าจาก collection
 */
public class ProductRepository {

    // ── In-memory storage ────────────────────────────────
    private final Map<String, Product> store = new ConcurrentHashMap<>();

    // ── Constructor: ใส่ข้อมูลตัวอย่าง ──────────────────
    public ProductRepository() {
        store.put("1", new Product("1", "iPhone 15 Pro (673380299-1 SEC 2)",
                "Electronics", "Apple", 50, 39900.0, "MEMBER"));
        store.put("2", new Product("2", "MacBook Air M3",
                "Electronics", "Apple", 20, 49900.0, "NONE"));
        store.put("3", new Product("3", "Samsung Galaxy S24",
                "Electronics", "Samsung", 30, 29900.0, "SEASONAL"));
    }

    // ── 1. หา Product 1 รายการ ───────────────────────────

    // ค้นหา Product จาก id
    // Mono เพราะผลลัพธ์มีได้ 0 หรือ 1 Product
    public Mono<Product> findById(String id) {
        Product product = store.get(id);

        if (product == null) {
            return Mono.empty(); // ถ้าไม่พบ ให้คืน Mono ว่าง
        }

        return Mono.just(product); // ถ้าพบ ให้ห่อ Product ด้วย Mono
    }

    // ── 2. หา Product ทั้งหมด ────────────────────────────
    // Flux เพราะสามารถมี Product หลายรายการ
    public Flux<Product> findAll() {
        return Flux.fromIterable(store.values());
    }

    // ── 3. บันทึก Product ────────────────────────────────
    public Mono<Product> save(Product product) {
        store.put(product.getId(), product);

        return Mono.just(product); // คืน Product ที่บันทึก
    }

    // ── 4. ลบ Product ────────────────────────────────────
    public Mono<Void> deleteById(String id) {
        store.remove(id);
        return Mono.empty(); // ไม่มีข้อมูลที่ต้องคืนหลังจากลบ
    }

    // ── 5. กรองตาม category ──────────────────────────────
    public Flux<Product> findByCategory(String category) {
        return findAll() // กรองเฉพาะ Product ที่ Category ตรงกัน
                .filter(p -> p.getCategory().equalsIgnoreCase(category));
    }
}
