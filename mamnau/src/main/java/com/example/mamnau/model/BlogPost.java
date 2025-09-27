package com.example.mamnau.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "blog_posts", indexes = {
        @Index(name="idx_blog_posts_slug", columnList = "slug", unique = true),
        @Index(name="idx_blog_posts_category", columnList = "category"),
        @Index(name="idx_blog_posts_status", columnList = "status"),
        @Index(name="idx_blog_posts_published_at", columnList = "published_at") // <-- Sửa ở đây
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BlogPost {

    public enum Status { DRAFT, PUBLISHED }

    public enum Category {
        MEO_LAM_VUON("Mẹo làm vườn"),
        TAI_CHE("Tái chế"),
        HUONG_DAN("Hướng dẫn"),
        BEN_VUNG("Bền vững");
        public final String label;
        Category(String label){ this.label = label; }
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false, unique=true)
    private String slug;

    @Column(columnDefinition="TEXT")
    private String excerpt;

    /** Nội dung HTML, render bằng th:utext ở post.html */
    @Column(columnDefinition="TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Category category;

    @Column(name = "cover_url")           // map rõ ràng
    private String coverUrl;

    @Column(name = "published_at")        // map rõ ràng
    private OffsetDateTime publishedAt;

    /** Tác giả bài viết */
    @Column(nullable=false)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    @Builder.Default
    private Status status = Status.DRAFT;

    /** Ước lượng phút đọc từ content/excerpt */
    @Transient
    public int getReadingMinutes() {
        String base = (content != null && !content.isBlank()) ? content : (excerpt != null ? excerpt : "");
        String plain = base.replaceAll("<[^>]+>", " ").trim();
        int words = plain.isEmpty() ? 0 : plain.split("\\s+").length;
        return Math.max(1, (int)Math.ceil(words / 200.0));
    }

    /** (tuỳ chọn) điền mặc định khi insert để tránh lỗi NOT NULL */
    @PrePersist
    void prePersist() {
        if (author == null || author.isBlank()) author = "Mầm Nâu Team";
        if (status == null) status = Status.DRAFT;
        if (publishedAt == null) publishedAt = OffsetDateTime.now();
    }
}
