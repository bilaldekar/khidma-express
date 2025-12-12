package dz.khidma.express.service;

import dz.khidma.express.entity.User;
import dz.khidma.express.model.security.ExtendedGenericResponse;
import dz.khidma.express.model.security.GenericResponse;
import dz.khidma.express.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    private final SecureRandom random = new SecureRandom();

    private static final long DEFAULT_TTL_SECONDS = 10 * 60; // 10 minutes

    private final Map<String, CodeInfo> codes = new ConcurrentHashMap<>();

    public ExtendedGenericResponse<String> sendVerificationCode(String email) {
        Optional<User> userOpt = Optional.empty();
        String targetEmail = email;

//        if (username != null && !username.isBlank()) {
//            userOpt = userRepository.findByUsername(username.trim());
//        }
        if (userOpt.isEmpty() && email != null && !email.isBlank()) {
            userOpt = userRepository.findByEmail(email.trim());
        }

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (targetEmail == null || targetEmail.isBlank()) {
                targetEmail = user.getEmail();
            }
        }

        if (targetEmail == null || targetEmail.isBlank()) {
            // For privacy, do not reveal whether user exists; respond success-like message
            log.warn("Verification email requested without resolvable target email (username={}, email provided={})", email);
            return new ExtendedGenericResponse<>(200, GenericResponse.SUCCESS, "If an account exists, a code will be sent.");
        }

        String code = generateCode();
        codes.put(targetEmail.toLowerCase(), new CodeInfo(code, Instant.now().plusSeconds(DEFAULT_TTL_SECONDS)));

        try {
//            emailService.sendSimpleMessage(targetEmail,
//                    "Your verification code",
//                    "Your verification code is: " + code + "\nIt expires in 10 minutes.");
            this.sendHtmlEmail(targetEmail, "KhidmaExpress verification code","Your KhidmaExpress verification code is: " + code + "\nIt expires in 10 minutes.");
        } catch (Exception ex) {
            log.error("Failed to send verification email to {}: {}", targetEmail, ex.getMessage());
            return new ExtendedGenericResponse<>(500, "Failed to send verification email", null);
        }

        return new ExtendedGenericResponse<>(200, GenericResponse.SUCCESS, "Verification code sent");
    }

    @Autowired
    private JavaMailSender mailSender;

    public void sendHtmlEmail(String to, String subject, String html) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
//        helper.setText(html, true);
        message.setText(html);
//        helper.setFrom("bilaldekar@gmail.com");

        mailSender.send(message);
    }

    public boolean verifyCode(String email, String code) {
        if (email == null || code == null) return false;
        CodeInfo info = codes.get(email.toLowerCase());
        if (info == null) return false;
        if (Instant.now().isAfter(info.expiresAt())) {
            codes.remove(email.toLowerCase());
            return false;
        }
        boolean match = info.code().equals(code);
        if (match) {
            codes.remove(email.toLowerCase());
            try {
                userRepository.findByEmailIgnoreCase(email.trim())
                        .ifPresent(u -> {
                            u.setEmailVerified(true);
                            userRepository.save(u);
                        });
            } catch (Exception e) {
                log.error("Failed to set emailVerified for {}: {}", email, e.getMessage());
                // Do not fail verification due to persistence error
            }
        }
        return match;
    }

    private String generateCode() {
        int n = random.nextInt(1_000_000); // 0..999999
        return String.format("%06d", n);
    }

    private record CodeInfo(String code, Instant expiresAt) {}
}
