// com/example/mamnau/controller/BlogController.java
package com.example.mamnau.controller;

import com.example.mamnau.model.BlogPost;
import com.example.mamnau.model.BlogPost.Category;
import com.example.mamnau.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    /** Inject các biến dùng chung cho navbar/footer/seo */
    @ModelAttribute
    public void addGlobals(Model model) {
        model.addAttribute("brandName", model.containsAttribute("brandName") ? model.getAttribute("brandName") : "Mầm Xanh");
        model.addAttribute("tagline", model.containsAttribute("tagline") ? model.getAttribute("tagline") : "phân bón từ vỏ trái cây");
    }

    @GetMapping("/blog")
    public String list(
            @RequestParam(name = "q",   required = false) String q,
            @RequestParam(name = "cat", required = false) Category cat,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ) {
        if (page < 0) page = 0;
        final int size = 4; // 4 bài/trang

        Page<BlogPost> posts = blogService.list(q, cat, page, size);

        // Nếu page quá lớn -> redirect về trang cuối (giữ query params)
        if (page > 0 && posts.getTotalPages() > 0 && page >= posts.getTotalPages()) {
            int last = posts.getTotalPages() - 1;
            String qParam   = (q   != null && !q.isBlank()) ? "&q=" + UriUtils.encode(q, StandardCharsets.UTF_8) : "";
            String catParam = (cat != null)                  ? "&cat=" + cat.name() : "";
            return "redirect:/blog?page=" + last + qParam + catParam;
        }

        // SEO cơ bản
        model.addAttribute("title", "Tin tức / Blog — Mầm Xanh");
        String desc = "Blog Mầm Xanh: mẹo làm vườn, tái chế vỏ trái cây, hướng dẫn sử dụng phân bón hữu cơ, câu chuyện bền vững.";
        if (q != null && !q.isBlank()) {
            desc = "Kết quả tìm kiếm cho \"" + q + "\" — Blog Mầm Xanh.";
        } else if (cat != null) {
            desc = "Chủ đề " + cat.name() + " — Tin bài tuyển chọn từ Blog Mầm Xanh.";
        }
        model.addAttribute("desc", desc);

        model.addAttribute("q", q);
        model.addAttribute("cat", cat);
        model.addAttribute("posts", posts);
        model.addAttribute("categories", Category.values());
        return "blog"; // tên template danh sách
    }

    @GetMapping("/blog/{slug}")
    public String detail(@PathVariable String slug, Model model) {
        BlogPost p = blogService.getBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy bài viết"));

        // Tính readingMinutes nếu service chưa set
        Integer readingMinutes = p.getReadingMinutes();
        if (readingMinutes == null || readingMinutes <= 0) {
            String text = p.getContent() == null ? "" : p.getContent().replaceAll("<[^>]+>", " ");
            int words = text.trim().isEmpty() ? 0 : text.trim().split("\\s+").length;
            readingMinutes = Math.max(1, (int) Math.ceil(words / 200.0)); // ~200 wpm
        }

        List<BlogPost> related = blogService.findRelated(p);
        if (related == null) related = List.of();

        // SEO cơ bản
        model.addAttribute("title", (p.getTitle() != null ? p.getTitle() : "Bài viết") + " — Blog Mầm Xanh");
        String desc = (p.getExcerpt() != null && !p.getExcerpt().isBlank())
                ? p.getExcerpt()
                : "Bài viết trên Blog Mầm Xanh về phân bón từ vỏ trái cây và lối sống xanh.";
        model.addAttribute("desc", desc);

        // Data cho template chi tiết
        model.addAttribute("p", p);
        model.addAttribute("readingMinutes", readingMinutes);
        model.addAttribute("related", related);

        return "blogdetail"; // tên template chi tiết
    }
}
