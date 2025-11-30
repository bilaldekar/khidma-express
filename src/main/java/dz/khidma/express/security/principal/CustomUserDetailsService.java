package dz.khidma.express.security.principal;

import dz.khidma.express.entity.Role;
import dz.khidma.express.entity.User;
import dz.khidma.express.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        CustomUserDetails cud = new CustomUserDetails();
        cud.setUsername(user.getUsername());
        cud.setPassword(user.getPassword());
        cud.setFirstName(user.getFirstName());
        cud.setLastName(user.getLastName());
        cud.setMail(user.getEmail());
        cud.setPhone(user.getPhoneNumber1());
        if (user.getExpiryDate() != null) {
            LocalDate exp = user.getExpiryDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            cud.setExpiryDate(exp);
        } else {
            // if no expiry date, consider not expired far in future
            cud.setExpiryDate(LocalDate.now().plusYears(100));
        }
        cud.setEnabled(Objects.equals(user.getEnabled(), 1));
        cud.setAccountNonLocked(user.getAccountNonLocked() == 1);
        // map roles to authorities safely
        Collection<? extends GrantedAuthority> authorities = mapAuthorities(user.getRoles());
        cud.setAuthorities(authorities);
        cud.setProvider("db");

        return new UserPrincipal(cud);
    }

    private Collection<? extends GrantedAuthority> mapAuthorities(List<Role> roles) {
        if (roles == null || roles.isEmpty()) return List.of();
        return roles.stream()
                .filter(Objects::nonNull)
                .map(Role::getRoleId)
                .filter(Objects::nonNull)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
