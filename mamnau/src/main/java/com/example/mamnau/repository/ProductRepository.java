package com.example.mamnau.repository;

import com.example.mamnau.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findTop6ByIsActiveTrueOrderByCreatedAtDesc();
    Optional<Product> findBySlugAndIsActiveTrue(String slug);
    // ✅ Thêm mới: tìm theo ID nhưng phải đang active
    Optional<Product> findByIdAndIsActiveTrue(Long id);
}
