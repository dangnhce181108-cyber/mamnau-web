// com/example/mamnau/controller/BlogController.java
package com.example.mamnau.controller;

import com.example.mamnau.model.BlogPost;
import com.example.mamnau.model.BlogPost.Category;
import com.example.mamnau.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    // --- tiện ích: truyền brandName, tagline, và mô tả mặc định ---
    private void addGlobals(Model model) {
        if (!model.containsAttribute("brandName")) {
            model.addAttribute("brandName", "Mầm Nâu");
        }
        if (!model.containsAttribute("tagline")) {
            model.addAttribute("tagline", "phân hữu cơ từ bã cà phê");
        }
    }

    @GetMapping("/blog")
    public String list(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "cat", required = false) Category cat,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ){
        int size = 4; // 4 bài/trang
        Page<BlogPost> posts = blogService.list(q, cat, page, size);

        // Nếu user gõ page quá lớn -> redirect về trang cuối
        if (page > 0 && posts.getTotalPages() > 0 && page >= posts.getTotalPages()) {
            int last = posts.getTotalPages() - 1;
            String qParam = (q != null && !q.isBlank())
                    ? "&q=" + org.springframework.web.util.UriUtils.encode(q, java.nio.charset.StandardCharsets.UTF_8)
                    : "";
            String catParam = (cat != null) ? "&cat=" + cat.name() : "";
            return "redirect:/blog?page=" + last + qParam + catParam;
        }

        // Globals cho navbar/footer
        addGlobals(model);

        // SEO title/desc
        model.addAttribute("title", "Tin tức / Blog — Mầm Nâu");
        String desc = "Blog Mầm Nâu: mẹo làm vườn, tái chế bã cà phê, hướng dẫn sử dụng phân hữu cơ, câu chuyện bền vững.";
        if (q != null && !q.isBlank()) {
            desc = "Kết quả tìm kiếm cho \"" + q + "\" — Blog Mầm Nâu.";
        } else if (cat != null) {
            desc = "Chủ đề " + cat.name() + " — Tin bài được tuyển chọn từ Blog Mầm Nâu.";
        }
        model.addAttribute("desc", desc);

        model.addAttribute("q", q);
        model.addAttribute("cat", cat);
        model.addAttribute("posts", posts);
        model.addAttribute("categories", Category.values());
        return "blog";
    }

    @GetMapping("/blog/{slug}")
    public String detail(@PathVariable String slug, Model model){
        var p = blogService.getBySlug(slug)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Không tìm thấy bài viết"));

        // Globals cho navbar/footer
        addGlobals(model);

        // SEO title/desc
        model.addAttribute("title", p.getTitle() + " — Blog Mầm Nâu");
        String desc = (p.getExcerpt() != null && !p.getExcerpt().isBlank())
                ? p.getExcerpt()
                : "Bài viết trên Blog Mầm Nâu về phân hữu cơ từ bã cà phê và lối sống xanh.";
        model.addAttribute("desc", desc);

        model.addAttribute("p", p);
        model.addAttribute("readingMinutes", p.getReadingMinutes());
        model.addAttribute("related", blogService.findRelated(p));
        return "blogdetail";
    }
}
