package com.example.mamnau.controller;

import com.example.mamnau.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final EmailService emailService;

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("brandName", "Mầm Nâu");
        model.addAttribute("tagline", "phân hữu cơ từ bã cà phê");
        return "contact"; // templates/contact.html (file bạn đã có)
    }

    @PostMapping("/contact")
    public String submitContact(@RequestParam("name") String name,
                                @RequestParam("email") String email,
                                @RequestParam("message") String message,
                                Model model) {
        boolean ok = emailService.sendContact(name, email, message);
        model.addAttribute("brandName", "Mầm Nâu");
        model.addAttribute("tagline", "phân hữu cơ từ bã cà phê");
        if (ok) {
            model.addAttribute("success", "Cảm ơn bạn! Chúng tôi đã nhận được liên hệ.");
        } else {
            model.addAttribute("error", "Gửi mail thất bại. Vui lòng thử lại sau.");
        }
        return "contact";
    }

    // ======== API cho trang Product Detail (popup gửi JSON) ========
    // Frontend gọi: fetch("/api/contact", { method:"POST", headers:{"Content-Type":"application/json"}, body: JSON.stringify({...}) })
    @PostMapping("/api/contact")
    @ResponseBody
    public ResponseEntity<?> productContact(@RequestBody Map<String, Object> body) {
        String name     = str(body.get("name"));
        String email    = str(body.get("email"));
        String phone    = str(body.get("phone"));
        String product  = str(body.get("product"));
        String message  = str(body.get("message"));
        String quantity = str(body.get("quantity")); // có thể rỗng

        // Validate cơ bản
        if (!StringUtils.hasText(name)) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "error", "Thiếu họ tên"));
        }
        if (!StringUtils.hasText(email) && !StringUtils.hasText(phone)) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "error", "Cần ít nhất email hoặc số điện thoại"));
        }

        // Gộp nội dung cho email (dùng template V2 của bạn)
        StringBuilder sb = new StringBuilder();
        if (StringUtils.hasText(product))  sb.append("🪴 Sản phẩm: ").append(esc(product)).append("\n");
        if (StringUtils.hasText(quantity)) sb.append("📦 Số lượng ước muốn: ").append(esc(quantity)).append("\n");
        if (StringUtils.hasText(phone))    sb.append("📞 Điện thoại/Zalo: ").append(esc(phone)).append("\n");
        if (StringUtils.hasText(message))  sb.append("\n— Ghi chú của khách —\n").append(message);

        boolean ok = emailService.sendContactV2(name, email, sb.toString());
        return ok
                ? ResponseEntity.ok(Map.of("ok", true))
                : ResponseEntity.status(500).body(Map.of("ok", false));
    }

    // ======== Helpers ========
    private String str(Object v){ return v == null ? "" : String.valueOf(v).trim(); }
    private String esc(String v){
        return v.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}

