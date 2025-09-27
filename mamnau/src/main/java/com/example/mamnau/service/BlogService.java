// com/example/mamnau/service/BlogService.java
package com.example.mamnau.service;

import com.example.mamnau.model.BlogPost;
import com.example.mamnau.model.BlogPost.Category;
import com.example.mamnau.model.BlogPost.Status;
import com.example.mamnau.repository.BlogPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogPostRepository repo;

    public Page<BlogPost> list(String q, Category cat, int page, int size){
        if (page < 0) page = 0;
        if (size <= 0) size = 6;
        if (size > 50) size = 50; // chặn user phá trang

        Sort sort = Sort.by(Sort.Order.desc("publishedAt").nullsLast());
        Pageable pageable = PageRequest.of(page, size, sort);
        return repo.search(Status.PUBLISHED, cat, q, pageable);
    }

    public Optional<BlogPost> getBySlug(String slug){
        return repo.findBySlugAndStatus(slug, BlogPost.Status.PUBLISHED);
    }

    public List<BlogPost> findRelated(BlogPost p){
        return repo.findTop3ByStatusAndCategoryAndIdNotOrderByPublishedAtDesc(
                BlogPost.Status.PUBLISHED, p.getCategory(), p.getId()
        );
    }
}
