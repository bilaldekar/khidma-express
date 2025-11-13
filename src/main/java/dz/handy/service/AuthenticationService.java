package dz.handy.service;

import dz.handy.entity.Role;
import dz.handy.entity.User;
import dz.handy.exception.AuthenticationException;
import dz.handy.model.security.ExtendedGenericResponse;
import dz.handy.model.security.GenericResponse;
import dz.handy.model.security.LoginResponse;
import dz.handy.model.security.RoleDTO;
import dz.handy.model.security.UserDTO;
import dz.handy.repository.RoleRepository;
import dz.handy.repository.UserRepository;
import dz.handy.security.jwt.JwtTokenUtil;
import dz.handy.security.principal.CustomUserDetails;
import dz.handy.security.principal.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationService {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private Environment env;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;

    public LoginResponse login(@RequestBody UserDTO request) {

        try {

                Authentication authenticate = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
                );

                UserPrincipal userPrincipal = (UserPrincipal) authenticate.getPrincipal();

                userRepository.updateLastLogin(request.getUsername());

                String token = jwtTokenUtil.generateAccessToken(userPrincipal.getUser());
                List<String> roles = jwtTokenUtil.getRoles(token.replace("Bearer ", ""));
                return new LoginResponse(200, GenericResponse.SUCCESS,
                        token,
                        roles,
                        userPrincipal.getUsername(),
                        userPrincipal.getUser().getFullName());



        } catch (BadCredentialsException ex) {
            throw new AuthenticationException(ex.getMessage(), 2, HttpStatus.UNAUTHORIZED);
        } catch (DisabledException ex) {
            throw new AuthenticationException(ex.getMessage(), 3, HttpStatus.UNAUTHORIZED);
        } catch (AccountExpiredException ex) {
            throw new AuthenticationException(ex.getMessage(), 4, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AuthenticationException(ex.getMessage(), 5, HttpStatus.UNAUTHORIZED);
        }
    }

    public ExtendedGenericResponse logout(String authorization) {

        jwtTokenUtil.invalidateToken(authorization);

        return new ExtendedGenericResponse(200, "Logout successful", null);
    }

    public ExtendedGenericResponse<User> register(UserDTO request) {
        if (request == null || request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return new ExtendedGenericResponse<>(400, "Username is null or empty", null);
        }
        String username = request.getUsername().trim();
        if(userRepository.findByUsername(username).isPresent()){
            return new ExtendedGenericResponse<>(400, "Username already exists", null);
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(request.getEmail());
        user.setPhoneNumber1(request.getPhoneNumber1());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCreationDate(new Date());
        // expiryDate from DTO if provided, else 1 year ahead
        Date expiry = null;
        LocalDate reqExpiry = request.getExpiryDate();
        if (reqExpiry != null) {
            expiry = Date.from(reqExpiry.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else {
            expiry = Date.from(LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        user.setExpiryDate(expiry);
        user.setEnabled(1);
        user.setAccountNonLocked(1);
        user.setAttempt(0);
        user.setLastLogin(null);

        // validate and encode password
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return new ExtendedGenericResponse<>(400, "Password is required", null);
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // handle roles (optional)
        if (request.getRoleDTOS() != null && !request.getRoleDTOS().isEmpty()) {
            List<String> roleIds = new ArrayList<>();
            Set<RoleDTO> roleDTOS = request.getRoleDTOS();
            for (RoleDTO r : roleDTOS) {
                if (r != null && r.getRoleId() != null) {
                    roleIds.add(r.getRoleId());
                }
            }
            if (!roleIds.isEmpty()) {
                List<Role> roles = roleRepository.findAllById(roleIds);
                user.setRoles(roles);
            }
        }

        User saved = userRepository.save(user);
        return new ExtendedGenericResponse<>(200, GenericResponse.SUCCESS, saved);
    }

    private CustomUserDetails convertUserDTOToCustomUserDetails(UserDTO userDTO) {
        CustomUserDetails customUserDetails = modelMapper.map(userDTO, CustomUserDetails.class);
        return customUserDetails;
    }

}