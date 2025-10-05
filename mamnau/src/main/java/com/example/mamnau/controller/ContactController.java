package com.example.mamnau.controller;

import com.example.mamnau.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
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
        model.addAttribute("brandName", "Mầm Xanh");
        model.addAttribute("tagline", "phân bón từ vỏ trái cây");
        return "contact"; // templates/contact.html
    }

    @PostMapping("/contact")
    public String submitContact(@RequestParam("name") String name,
                                @RequestParam("email") String email,
                                @RequestParam("message") String message,
                                Model model) {
        boolean ok = emailService.sendContact(name, email, message);
        model.addAttribute("brandName", "Mầm Xanh");
        model.addAttribute("tagline", "phân bón từ vỏ trái cây");
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
        String from     = str(body.get("from"));     // lấy cờ từ frontend

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
        // ✅ Chỉ thêm số lượng nếu from == "detail"
        if ("detail".equalsIgnoreCase(from) && StringUtils.hasText(quantity))
            sb.append("📦 Số lượng ước muốn: ").append(esc(quantity)).append("\n");
        if (StringUtils.hasText(phone))    sb.append("📞 Điện thoại/Zalo: ").append(esc(phone)).append("\n");
        if (StringUtils.hasText(message))  sb.append("\n— Ghi chú của khách —\n").append(message);

        boolean ok = emailService.sendContactV2(name, email, sb.toString());
        return ok
                ? ResponseEntity.ok(Map.of("ok", true))
                : ResponseEntity.status(500).body(Map.of("ok", false));
    }

    // ======== API cho trang Home (Gửi đăng kí) ========
    @PostMapping("/api/newsletter-signup")
    @ResponseBody
    public ResponseEntity<?> newsletterSignup(@RequestBody Map<String, Object> body,
                                              HttpServletRequest req) {
        String email = str(body.get("email"));

        if (!StringUtils.hasText(email)) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "error", "Thiếu email"));
        }
        // Check định dạng đơn giản
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "error", "Email không hợp lệ"));
        }

        String ua = str(req.getHeader("User-Agent"));
        String ip = str(req.getHeader("X-Forwarded-For"));
        if (!StringUtils.hasText(ip)) ip = str(req.getRemoteAddr());

        boolean ok = emailService.sendNewsletterSignup(email, ua, ip);
        return ok
                ? ResponseEntity.ok(Map.of("ok", true))
                : ResponseEntity.status(500).body(Map.of("ok", false, "error", "Không gửi được email"));
    }

    // ======== Helpers ========
    private String str(Object v){ return v == null ? "" : String.valueOf(v).trim(); }
    private String esc(String v){
        return v.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
