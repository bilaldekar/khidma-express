package dz.handy.service;

import dz.handy.entity.User;
import dz.handy.model.security.ExtendedGenericResponse;
import dz.handy.model.security.GenericResponse;
import dz.handy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public ExtendedGenericResponse<String> sendVerificationCode(String username, String email) {
        Optional<User> userOpt = Optional.empty();
        String targetEmail = email;

        if (username != null && !username.isBlank()) {
            userOpt = userRepository.findByUsername(username.trim());
        }
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
            log.warn("Verification email requested without resolvable target email (username={}, email provided={})", username, email);
            return new ExtendedGenericResponse<>(200, GenericResponse.SUCCESS, "If an account exists, a code will be sent.");
        }

        String code = generateCode();
        codes.put(targetEmail.toLowerCase(), new CodeInfo(code, Instant.now().plusSeconds(DEFAULT_TTL_SECONDS)));

        try {
            emailService.sendSimpleMessage(targetEmail,
                    "Your verification code",
                    "Your verification code is: " + code + "\nIt expires in 10 minutes.");
        } catch (Exception ex) {
            log.error("Failed to send verification email to {}: {}", targetEmail, ex.getMessage());
            return new ExtendedGenericResponse<>(500, "Failed to send verification email", null);
        }

        return new ExtendedGenericResponse<>(200, GenericResponse.SUCCESS, "Verification code sent");
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
        }
        return match;
    }

    private String generateCode() {
        int n = random.nextInt(1_000_000); // 0..999999
        return String.format("%06d", n);
    }

    private record CodeInfo(String code, Instant expiresAt) {}
}
