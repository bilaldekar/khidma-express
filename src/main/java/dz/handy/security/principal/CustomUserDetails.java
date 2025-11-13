package dz.handy.security.principal;


import dz.handy.model.security.RoleDTO;
import dz.handy.model.security.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails {
    String username;
    String password;
    String firstName;
    String fullName;
    String lastName;
    String mail;
    String phone;
    LocalDate expiryDate;
    boolean enabled;
    boolean accountNonLocked;
    Collection<? extends GrantedAuthority> authorities;
    String provider;

    public CustomUserDetails(UserDTO userEntity){
        this.username = userEntity.getUsername();
        this.password = userEntity.getPassword();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.mail = userEntity.getEmail();
        this.phone = userEntity.getPhoneNumber1();
        this.expiryDate = userEntity.getExpiryDate();
        this.enabled = userEntity.getEnabled() == 1 ? true : false;
        this.accountNonLocked = userEntity.getEnabled() == 1 ? true : false;
        setAuthoritiesFromRoles(userEntity.getRoleDTOS());
        this.provider = "db";

    }

    public void setAuthoritiesFromRoles(Set<RoleDTO> roleDTOS){
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleDTO roleDTO : roleDTOS) {
            authorities.add(new SimpleGrantedAuthority(roleDTO.getRoleId()));
        }
        this.authorities = authorities;
    }

    public String getFullName(){
        return this.getFirstName() + " " + this.getLastName();
    }

}