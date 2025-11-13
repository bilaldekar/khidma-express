package dz.handy.model.security;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse extends GenericResponse {
    private String authorization;
    private String username;
    private String fullName;
    private List<String> roles;

    public LoginResponse(int code, String message, String authorization, List<String> roles, String username, String fullName) {
        super(code, message);
        this.authorization = authorization;
        this.roles = roles;
        this.username = username;
        this.fullName = fullName;
    }

}