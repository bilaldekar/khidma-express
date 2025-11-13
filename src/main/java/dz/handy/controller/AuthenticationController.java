package dz.handy.controller;

import dz.handy.entity.User;
import dz.handy.model.security.ExtendedGenericResponse;
import dz.handy.model.security.LoginResponse;
import dz.handy.model.security.UserDTO;
import dz.handy.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api")
public class AuthenticationController {

    AuthenticationService authenticationService;


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
}