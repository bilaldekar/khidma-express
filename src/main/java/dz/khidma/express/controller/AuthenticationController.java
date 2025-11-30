package dz.khidma.express.controller;

import dz.khidma.express.entity.User;
import dz.khidma.express.model.security.ExtendedGenericResponse;
import dz.khidma.express.model.security.LoginResponse;
import dz.khidma.express.model.security.UserDTO;
import dz.khidma.express.model.security.VerificationCodeRequest;
import dz.khidma.express.model.security.VerificationEmailRequest;
import dz.khidma.express.service.AuthenticationService;
import dz.khidma.express.service.VerificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    AuthenticationService authenticationService;
    VerificationService verificationService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody UserDTO request) {
        return authenticationService.login(request);
    }

    @PostMapping("/logout")
    public ExtendedGenericResponse logout(@RequestHeader("Authorization") String token) {
        return authenticationService.logout(token);
    }

    @PostMapping("/register")
    public ExtendedGenericResponse<User> register(@RequestBody UserDTO request) {
        return authenticationService.register(request);
    }

    @PostMapping("/verification/email")
    public ExtendedGenericResponse<String> sendVerificationEmail(@RequestBody VerificationEmailRequest request) {
        String username = request.getUsername();
        String email = request.getEmail();
        return verificationService.sendVerificationCode(username, email);
    }

    @PostMapping("/verification/email/confirm")
    public ExtendedGenericResponse<String> confirmVerificationCode(@RequestBody VerificationCodeRequest request) {
        boolean ok = verificationService.verifyCode(request.getEmail(), request.getCode());
        if (ok) {
            return new ExtendedGenericResponse<>(200, "Verification successful", "OK");
        } else {
            return new ExtendedGenericResponse<>(400, "Invalid or expired code", null);
        }
    }
}