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
        model.addAttribute("brandName", "Mầm Nâu");
        model.addAttribute("tagline", "phân hữu cơ từ bã cà phê");
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
        model.addAttribute("title", p.getName() + " — Mầm Nâu");
        model.addAttribute("desc",
                (p.getShortDesc() != null && !p.getShortDesc().isBlank())
                        ? p.getShortDesc()
                        : "Chi tiết sản phẩm Mầm Nâu");
        model.addAttribute("brandName", "Mầm Nâu");
        model.addAttribute("tagline", "phân hữu cơ từ bã cà phê");
    }

    @GetMapping("/faq")
    public String faq(Model model) {
        model.addAttribute("title", "FAQ — Mầm Nâu");
        model.addAttribute("brandName", "Mầm Nâu");
        model.addAttribute("tagline", "phân hữu cơ từ bã cà phê"); // ⬅️ thêm dòng này
        model.addAttribute("desc", "Câu hỏi thường gặp về phân hữu cơ từ bã cà phê Mầm Nâu: sử dụng, bảo quản, an toàn, đặt mua.");
        return "faq";
    }


    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("brandName", "Mầm Nâu");
        model.addAttribute("tagline", "phân hữu cơ từ bã cà phê");
        model.addAttribute("title", "Về chúng tôi — Mầm Nâu");
        model.addAttribute("desc", "Giới thiệu Mầm Nâu: hành trình biến bã cà phê thành phân hữu cơ, sứ mệnh và tầm nhìn xanh.");
        return "about"; // templates/about.html
    }
}
