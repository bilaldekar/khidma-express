package dz.khidma.express.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class User {

    @Id
    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PHONE_NUMBER1")
    private String phoneNumber1;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "EXPIRY_DATE")
    private Date expiryDate;

    @Column(name = "ENABLED")
    private Integer enabled;



    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "WILAYA")
    private String wilaya;

    @Column(name = "COMMUNE")
    private String commune;

//    @Column(name = "PASSWORD_CHANGED")
//    private Integer passwordChanged;

    @Column(name = "ATTEMPT_DATE")
    @JsonIgnore
    private Date attemptDate;

    @Column(name = "ATTEMPT")
    @JsonIgnore
    private Integer attempt;

    @Column(name = "ACCOUNT_NON_LOCKED")
    private int accountNonLocked;

    @Column(name = "PASSWORD")
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "LAST_LOGIN")
    private Date lastLogin;

//    @Transient
//    String newPassword;
//
//    @Transient
//    String oldPassword;

    @ManyToMany
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JoinTable(name = "USERS_ROLES", joinColumns = @JoinColumn(name = "USERNAME"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private List<Role> roles;

    public String getFullName(){
        String fn = this.getFirstName() != null ? this.getFirstName().trim() : "";
        String ln = this.getLastName() != null ? this.getLastName().trim() : "";
        if (!fn.isEmpty() && !ln.isEmpty()) return fn + " " + ln;
        return (fn + " " + ln).trim();
    }

    @Override
    public String toString() {
        return "UserEntity [USERNAME=" + username + ", EMAIL=" + email +
                ", PHONE_NUMBER1=" + phoneNumber1  +
                ", FIRST_NAME=" + firstName + ", LAST_NAME=" + lastName +
                " ]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
