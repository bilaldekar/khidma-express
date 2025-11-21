package dz.handy.model.security;

import lombok.Data;

@Data
public class VerificationCodeRequest {
    private String email;
    private String code;
}
