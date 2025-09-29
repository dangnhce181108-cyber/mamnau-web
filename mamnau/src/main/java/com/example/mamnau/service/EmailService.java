package com.example.mamnau.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${app.contact.to}")
    private String to; // admin nhận mail

    /** UI 1 (GIỮ NGUYÊN): Template sáng, viền xanh nhạt */
    // Trang Lien He
    public boolean sendContact(String name, String email, String message) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");

            helper.setTo(to);
            helper.setFrom(from);
            if (email != null && !email.isBlank()) helper.setReplyTo(email);
            helper.setSubject("📩 Liên hệ/FAQ mới từ " + safe(name) + " — Mầm Nâu");

            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            String html = """
                    <!doctype html>
                    <html lang="vi">
                    <head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
                    <title>Liên hệ mới - Mầm Nâu</title></head>
                    <body style="margin:0;padding:24px;background:#f7fbf7;font-family:'Segoe UI',Roboto,Arial,sans-serif;">
                      <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="max-width:680px;margin:0 auto;background:#fff;border-radius:14px;overflow:hidden;border:1px solid #e6efe8">
                        <tr>
                          <td style="background:linear-gradient(135deg,#eaf4ee,#fff);padding:24px 28px;border-bottom:1px solid #e6efe8">
                            <div style="font-size:22px;font-weight:800;color:#2e7d32">🌱 Mầm Nâu</div>
                            <div style="color:#6b4f3b;font-weight:600">phân hữu cơ từ bã cà phê</div>
                          </td>
                        </tr>
                        <tr>
                          <td style="padding:24px 28px">
                            <h2 style="margin:0 0 10px 0;color:#243522;font-size:20px">📬 Liên hệ/FAQ của khách hàng</h2>
                            <p style="margin:0;color:#5b6b57;font-size:14px">Thời gian: <strong>%s</strong></p>

                            <div style="margin:18px 0;padding:16px;border:1px solid #e6efe8;border-radius:12px;background:#fafdfb">
                              <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="font-size:15px;color:#243522">
                                <tr>
                                  <td style="padding:8px 0;width:160px;color:#5b6b57">Họ và tên</td>
                                  <td style="padding:8px 0"><strong>%s</strong></td>
                                </tr>
                                <tr>
                                  <td style="padding:8px 0;color:#5b6b57">Email</td>
                                  <td style="padding:8px 0"><a href="mailto:%s" style="color:#2e7d32;text-decoration:none">%s</a></td>
                                </tr>
                              </table>
                            </div>

                            <div style="margin-top:12px">
                              <div style="font-weight:700;margin-bottom:6px;color:#243522">📝 Nội dung</div>
                              <div style="white-space:pre-wrap;line-height:1.6;color:#243522;border:1px solid #e6efe8;border-radius:12px;padding:14px;background:#fff">
                                %s
                              </div>
                            </div>

                            <p style="margin-top:18px;color:#5b6b57;font-size:13px">Nhấn <strong>Reply</strong> để phản hồi trực tiếp cho khách.</p>
                          </td>
                        </tr>
                        <tr>
                          <td style="background:#0e1a0f;color:#d7e3d7;padding:16px 28px;font-size:12px;text-align:center">
                            © %s Mầm Nâu — Vì một hành tinh nhiều mảng xanh hơn.
                          </td>
                        </tr>
                      </table>
                    </body>
                    </html>
                    """.formatted(now, safe(name), safe(email), safe(email), safe(message), String.valueOf(LocalDateTime.now().getYear()));

            helper.setText(html, true);
            mailSender.send(msg);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** UI 2 (MỚI): “Coffee Card” — header nâu, thẻ kính mờ, block thông tin rõ ràng */
    // navbar và product detail
    public boolean sendContactV2(String name, String email, String message) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");

            helper.setTo(to);
            helper.setFrom(from);
            if (email != null && !email.isBlank()) helper.setReplyTo(email);
            helper.setSubject("☕ Đặt Hàng — Mầm Nâu (Coffee Card)");

            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            String year = String.valueOf(LocalDateTime.now().getYear());

            String html = """
                    <!doctype html>
                    <html lang="vi">
                      <head>
                        <meta charset="utf-8">
                        <meta name="viewport" content="width=device-width,initial-scale=1">
                        <title>Liên hệ — Coffee Card</title>
                      </head>
                      <body style="margin:0;padding:0;background:#f4f1ee;font-family:'Segoe UI',Roboto,Arial,sans-serif;color:#2b2b2b;">
                        <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="background:#f4f1ee;padding:24px 12px;">
                          <tr>
                            <td>
                              <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="max-width:720px;margin:0 auto;border-radius:18px;overflow:hidden;box-shadow:0 12px 30px rgba(0,0,0,.10);">
                                <!-- Header nâu -->
                                <tr>
                                  <td style="background:linear-gradient(135deg,#5a3d2b,#3e2a20);padding:28px 28px 24px 28px;color:#fff;">
                                    <div style="font-size:22px;font-weight:800;letter-spacing:.3px">☕ Mầm Nâu</div>
                                    <div style="opacity:.9">phân hữu cơ từ bã cà phê</div>
                                  </td>
                                </tr>

                                <!-- Body kính mờ -->
                                <tr>
                                  <td style="padding:0;background:#efe9e3;">
                                    <div style="padding:24px;">
                                      <div style="backdrop-filter:saturate(160%%) blur(4px);background:rgba(255,255,255,.82);border:1px solid rgba(0,0,0,.06);border-radius:16px;padding:22px;">
                                        <h2 style="margin:0 0 8px 0;font-size:20px;">📬 Đặt Hàng </h2>
                                        <p style="margin:0 0 14px 0;color:#5b5b5b;font-size:14px">Thời gian: <strong>%s</strong></p>

                                        <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="border-collapse:separate;border-spacing:0 8px;font-size:15px;">
                                          <tr>
                                            <td style="width:170px;color:#6b4f3b;font-weight:700;vertical-align:top;">Họ và tên</td>
                                            <td style="vertical-align:top;"><strong>%s</strong></td>
                                          </tr>
                                          <tr>
                                            <td style="color:#6b4f3b;font-weight:700;vertical-align:top;">Email</td>
                                            <td style="vertical-align:top;"><a href="mailto:%s" style="color:#2e7d32;text-decoration:none">%s</a></td>
                                          </tr>
                                        </table>

                                        <div style="margin-top:12px">
                                          <div style="font-weight:800;margin-bottom:6px;color:#2b2b2b">📝 Nội dung</div>
                                          <div style="white-space:pre-wrap;line-height:1.65;color:#2b2b2b;border:1px dashed #d8c9bc;border-radius:12px;padding:14px;background:#fffdfb">
                                            %s
                                          </div>
                                        </div>

                                        <p style="margin:16px 0 0 0;font-size:13px;color:#6b4f3b;">Mẹo: nhấn <strong>Reply</strong> để trả lời khách.</p>
                                      </div>
                                    </div>
                                  </td>
                                </tr>

                                <!-- Footer -->
                                <tr>
                                  <td style="background:#0e1a0f;color:#d7e3d7;padding:16px 28px;text-align:center;font-size:12px;">
                                    © %s Mầm Nâu — Tái chế bã cà phê, gieo mầm xanh.
                                  </td>
                                </tr>
                              </table>
                            </td>
                          </tr>
                        </table>
                      </body>
                    </html>
                    """.formatted(
                    now,
                    safe(name),
                    safe(email), safe(email),
                    safe(message),
                    year
            );

            helper.setText(html, true);
            mailSender.send(msg);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }


    // ======== API cho trang Home (Khung Đăng kí) ========
    public boolean sendNewsletterSignup(String email, String userAgent, String ip) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");

            helper.setTo(to);           // Admin nhận thông báo
            helper.setFrom(from);
            helper.setReplyTo(email);
            helper.setSubject("🆕 Đăng ký nhận thông tin dùng thử — " + safe(email));

            String now = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            String year = String.valueOf(LocalDateTime.now().getYear());

            String html = """
<!doctype html>
<html lang="vi">
<head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Đăng ký dùng thử</title></head>
<body style="margin:0;padding:24px;background:#f7fbf7;font-family:'Segoe UI',Roboto,Arial,sans-serif;">
  <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="max-width:680px;margin:0 auto;background:#fff;border-radius:14px;overflow:hidden;border:1px solid #e6efe8">
    <tr>
      <td style="background:linear-gradient(135deg,#eaf4ee,#fff);padding:24px 28px;border-bottom:1px solid #e6efe8">
        <div style="font-size:22px;font-weight:800;color:#2e7d32">🌱 Mầm Nâu</div>
        <div style="color:#6b4f3b;font-weight:600">phân hữu cơ từ bã cà phê</div>
      </td>
    </tr>
    <tr>
      <td style="padding:24px 28px">
        <h2 style="margin:0 0 10px 0;color:#243522;font-size:20px">📬 Đăng ký nhận thông tin dùng thử</h2>
        <p style="margin:0;color:#5b6b57;font-size:14px">Thời gian: <strong>%s</strong></p>

        <div style="margin:18px 0;padding:16px;border:1px solid #e6efe8;border-radius:12px;background:#fafdfb">
          <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="font-size:15px;color:#243522">
            <tr>
              <td style="padding:8px 0;width:160px;color:#5b6b57">Email</td>
              <td style="padding:8px 0">
                <a href="mailto:%s" style="color:#2e7d32;text-decoration:none">%s</a>
              </td>
            </tr>
            <!-- ĐÃ XOÁ 2 hàng User-Agent và IP -->
          </table>
        </div>

        <p style="margin-top:12px;color:#5b6b57;font-size:13px">
          Hãy gửi bộ <strong>thông tin sản phẩm thử nghiệm</strong> và ưu đãi sớm cho khách này nhé.
        </p>
      </td>
    </tr>
    <tr>
      <td style="background:#0e1a0f;color:#d7e3d7;padding:16px 28px;font-size:12px;text-align:center">
        © %s Mầm Nâu — Vì một hành tinh nhiều mảng xanh hơn.
      </td>
    </tr>
  </table>
</body>
</html>
""".formatted(
                    now,
                    safe(email), safe(email),
                    year
            );

            helper.setText(html, true);
            mailSender.send(msg);

            // (Tuỳ chọn) gửi mail xác nhận cho người đăng ký
            try { sendConfirmToUser(email); } catch (Exception ignored) {}

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sendConfirmToUser(String email) throws MessagingException {
        MimeMessage msg2 = mailSender.createMimeMessage();
        MimeMessageHelper h2 = new MimeMessageHelper(msg2, true, "UTF-8");
        h2.setTo(email);
        h2.setFrom(from);
        h2.setSubject("🌱 Mầm Nâu — Đã nhận đăng ký của bạn");

        String html = """
            <div style="font-family:'Segoe UI',Roboto,Arial,sans-serif;color:#243522">
              <p>Chào bạn,</p>
              <p>Chúng tôi đã nhận được đăng ký nhận thông tin dùng thử. Team Mầm Nâu sẽ gửi chi tiết sản phẩm sớm nhất.</p>
              <p>Trân trọng,<br/>Mầm Nâu</p>
            </div>
            """;
        h2.setText(html, true);
        mailSender.send(msg2);
    }

    // ===== Helpers =====
    private String safe(String s){ return s == null ? "" : escape(s); }
    private String escape(String s) {
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
