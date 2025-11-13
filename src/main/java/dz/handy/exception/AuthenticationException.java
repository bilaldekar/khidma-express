package dz.handy.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AuthenticationException extends RuntimeException {

    public static final String INVALID_USERNAME = "auth.exception.invalidUsername";

    public static final String BAD_CREDENTIAL = "auth.exception.badCredential";

    Integer errorCode;

    HttpStatus httpStatus;

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Integer errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }


}
