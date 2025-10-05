package com.example.mamnau.controller;

import com.example.mamnau.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;

    @GetMapping("/")
    public String landing(Model model) {
        model.addAttribute("brandName", "Mầm Xanh");
        model.addAttribute("tagline", "phân bón từ vỏ trái cây");
        model.addAttribute("products", productService.getLatestActiveProducts());
        return "home";
    }

    // Xem sản phẩm bằng slug (đang có)
    @GetMapping("/san-pham/{slug}")
    public String productDetailBySlug(@PathVariable String slug, Model model) {
        var p = productService.getActiveBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"));
        addCommonAttributes(model, p);
        return "product-detail";
    }

    // ✅ Xem sản phẩm bằng id
    @GetMapping("/san-pham/id/{id}")
    public String productDetailById(@PathVariable Long id, Model model) {
        var p = productService.getActiveById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"));
        addCommonAttributes(model, p);
        return "product-detail";
    }

    private void addCommonAttributes(Model model, com.example.mamnau.model.Product p) {
        model.addAttribute("p", p);
        model.addAttribute("title", p.getName() + " — Mầm Xanh");
        model.addAttribute("desc",
                (p.getShortDesc() != null && !p.getShortDesc().isBlank())
                        ? p.getShortDesc()
                        : "Chi tiết sản phẩm Mầm Xanh (phân bón từ vỏ trái cây)");
        model.addAttribute("brandName", "Mầm Xanh");
        model.addAttribute("tagline", "phân bón từ vỏ trái cây");
    }

    @GetMapping("/faq")
    public String faq(Model model) {
        model.addAttribute("title", "FAQ — Mầm Xanh");
        model.addAttribute("brandName", "Mầm Xanh");
        model.addAttribute("tagline", "phân bón từ vỏ trái cây");
        model.addAttribute("desc", "Câu hỏi thường gặp về phân bón Mầm Xanh: tái chế vỏ trái cây từ chợ thành phân hữu cơ, cách dùng, bảo quản, an toàn, đặt mua.");
        return "faq";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("brandName", "Mầm Xanh");
        model.addAttribute("tagline", "phân bón từ vỏ trái cây");
        model.addAttribute("title", "Về chúng tôi — Mầm Xanh");
        model.addAttribute("desc", "Giới thiệu Mầm Xanh: hành trình tái chế vỏ trái cây ở chợ thành phân bón hữu cơ, sứ mệnh và tầm nhìn xanh.");
        return "about"; // templates/about.html
    }
}
