package com.example.lab10;

import com.example.lab10.model.Product;
import com.example.lab10.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

/**
 * Lab10ApplicationTests — ทดสอบ Reactive code
 *
 * StepVerifier — วิธีทดสอบ Mono/Flux
 */
@SpringBootTest
class Lab10ApplicationTests {

    @Autowired
    private ProductRepository repository;

    @Test
    void contextLoads() {
        // Spring Application Context โหลดสำเร็จ
    }

    @Test
    void testFindById_found() {
        StepVerifier.create(repository.findById("1"))
                .expectNextMatches(p -> p.getName().contains("iPhone"))
                .verifyComplete();
    }

    @Test
    void testFindById_notFound() {
        StepVerifier.create(repository.findById("999"))
                .verifyComplete();
    }

    @Test
    void testFindAll() {
        StepVerifier.create(repository.findAll())
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void testSave() {

        Product product = new Product(
                "test-100",
                "Test Product",
                "TestCategory",
                "TestBrand",
                10,
                1000.0,
                "NONE"
        );

        StepVerifier.create(
                repository.save(product)
                        .flatMap(saved ->
                                repository.deleteById(saved.getId())
                                        .thenReturn(saved)
                        )
        )
                .expectNextMatches(p ->
                        p.getId().equals("test-100")
                                && p.getName().equals("Test Product")
                                && p.getPrice().equals(1000.0)
                )
                .verifyComplete();
    }

    @Test
    void testFindByCategory() {
        StepVerifier.create(
                repository.findByCategory("Electronics")
        )
                .expectNextCount(3)
                .verifyComplete();
    }
}