package dz.khidma.express.model.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExtendedGenericResponse<T> extends GenericResponse implements Serializable {

    private T result;

    public ExtendedGenericResponse(int code, String message, T extraObject) {
        super(code, message);
        this.result = extraObject;
    }
}
