// com/example/mamnau/repository/BlogPostRepository.java
package com.example.mamnau.repository;

import com.example.mamnau.model.BlogPost;
import com.example.mamnau.model.BlogPost.Category;
import com.example.mamnau.model.BlogPost.Status;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    Optional<BlogPost> findBySlugAndStatus(String slug, BlogPost.Status status);

    List<BlogPost> findTop3ByStatusAndCategoryAndIdNotOrderByPublishedAtDesc(
            BlogPost.Status status,
            BlogPost.Category category,
            Long id
    );

    @Query("""
      SELECT p FROM BlogPost p
      WHERE p.status = :status
        AND (:category IS NULL OR p.category = :category)
        AND (
            :q IS NULL OR :q = '' OR
            lower(p.title)   LIKE lower(concat('%', :q, '%')) OR
            lower(p.excerpt) LIKE lower(concat('%', :q, '%')) OR
            lower(p.content) LIKE lower(concat('%', :q, '%'))
        )
      """)
    Page<BlogPost> search(
            @Param("status") Status status,
            @Param("category") Category category,
            @Param("q") String q,
            Pageable pageable
    );
}
