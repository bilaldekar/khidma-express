package dz.handy.model.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String username;

    private String email;

    private String phoneNumber1;

    private String firstName;

    private String lastName;

    private LocalDate creationDate;

    private LocalDate expiryDate;

    private Integer enabled;

    private Double latitude;

    private Double longitude;


    @JsonIgnore
    private Date attemptDate;

    @JsonIgnore
    private Integer attempt;

    private int accountNonLocked;

    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Date lastLogin;


    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private Set<RoleDTO> roleDTOS;


    public UserDTO(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserEntity [USERNAME=" + username + ", EMAIL=" + email +
                ", PHONE_NUMBER1=" + phoneNumber1 +
                ", FIRST_NAME=" + firstName + ", LAST_NAME=" + lastName +

                " ]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO user = (UserDTO) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
