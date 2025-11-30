package dz.khidma.express.model.security;

import lombok.Data;

@Data
public class VerificationCodeRequest {
    private String email;
    private String code;
}
