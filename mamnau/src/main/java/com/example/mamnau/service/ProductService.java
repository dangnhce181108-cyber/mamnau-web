package com.example.mamnau.service;

import com.example.mamnau.model.Product;
import com.example.mamnau.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getLatestActiveProducts() {
        return productRepository.findTop6ByIsActiveTrueOrderByCreatedAtDesc();
    }
    public Optional<Product> getActiveBySlug(String slug) {
        return productRepository.findBySlugAndIsActiveTrue(slug);
    }

    // ✅ Thêm mới: lấy theo ID (chỉ sản phẩm đang active)
    public Optional<Product> getActiveById(Long id) {
        return productRepository.findByIdAndIsActiveTrue(id);
    }
}
