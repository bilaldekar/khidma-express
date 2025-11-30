package dz.khidma.express.model.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class GenericResponse implements Serializable {

    public static final String SUCCESS = "Success";
    public static final String ERROR = "Error";
    public static final String ALREADY_USED = "already_Used";

    private int code;
    private String message;

    public GenericResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
