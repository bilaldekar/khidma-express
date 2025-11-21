package dz.handy.model.security;

import lombok.Data;

@Data
public class VerificationEmailRequest {
    private String username;
    private String email;
}
