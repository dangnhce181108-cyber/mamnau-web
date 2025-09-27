package com.example.mamnau.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                          // Khóa chính, tự tăng

    @Column(nullable = false)
    private String name;                      // Tên sản phẩm

    @Column(nullable = false, unique = true)
    private String slug;                      // Định danh URL

    @Column(name = "short_desc", nullable = false, columnDefinition = "TEXT")
    private String shortDesc;                 // Mô tả ngắn (lead)

    @Column(name = "long_desc", columnDefinition = "TEXT")
    private String longDesc;                  // Mô tả dài (chi tiết)

    // Các thuộc tính nội dung
    @Column(columnDefinition = "TEXT")
    private String formula;                   // Công thức

    @Column(columnDefinition = "TEXT")
    private String instructions;              // Hướng dẫn sử dụng

    @Column(columnDefinition = "TEXT")
    private String notes;                      // Lưu ý

    @Column(columnDefinition = "TEXT")
    private String commitments;               // Cam kết

    @Column(name = "green_story", columnDefinition = "TEXT")
    private String greenStory;                // Câu chuyện xanh

    @Column(name = "brand_story", columnDefinition = "TEXT")
    private String brandStory;                // Câu chuyện thương hiệu Mầm Nâu

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;                 // Giá (VND)

    @Column(name = "img_url")
    private String imgUrl;                    // Ảnh minh họa

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;          // Bật/tắt hiển thị

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;         // Ngày tạo

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;         // Ngày cập nhật
}
