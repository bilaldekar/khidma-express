package dz.handy.security.jwt;


import dz.handy.model.security.JwtValidationModel;
import dz.handy.security.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenUtil jwtTokenUtil;

    private CacheManager cacheManager;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("**** --- JwtTokenFilter --- ****");
        // log access
        String method = request.getMethod();
        // Create a methodAndPath like [GET]/api/register-request
        String path = new UrlPathHelper().getPathWithinApplication(request);
        String methodAndPath = "[" + method + "]" + path;
        log.info("Requested URL {}", methodAndPath);
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            this.writeResponse(response, HttpServletResponse.SC_OK, "Pre-flight Request accepted");
        }
        // Get authorization header and validate
        final String header = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();

        JwtValidationModel jwtValidation = jwtTokenUtil.validate(token);
        if (!jwtValidation.isStatus()) {
            this.writeResponse(response, HttpServletResponse.SC_UNAUTHORIZED, jwtValidation.getMessage());
            return;
        }

        // Check if the authorization is not blacklisted (invalidated)
        Cache cache = cacheManager.getCache("invalidatedTokensCache");
        if (cache.get(header) != null) {
            this.writeResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token invalid");
            return;
        }

        HttpServletRequest httpServletRequest = request;
        httpServletRequest.setAttribute("username", jwtTokenUtil.getUsername(token));
        httpServletRequest.setAttribute("roles", jwtTokenUtil.getRoles(token));

        // log access
        method = request.getMethod();
        // Create a methodAndPath like [GET]/api/register-request
        path = new UrlPathHelper().getPathWithinApplication(request);
        methodAndPath = "[" + method + "]" + path;
        log.info("Requested URL {} by user {}", methodAndPath, jwtTokenUtil.getUsername(token));

        Set<SimpleGrantedAuthority> authorities = jwtTokenUtil.getRoles(token).stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toSet());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                jwtTokenUtil.getUsername(token), null, authorities
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(httpServletRequest, response);
    }

    private void writeResponse(HttpServletResponse httpResponse, int code, String message) throws IOException {
        httpResponse.setContentType("application/json");
        httpResponse.setStatus(code);
        httpResponse.getOutputStream().write(("{\"code\":" + code + ",").getBytes());
        httpResponse.getOutputStream().write(("\"message\":\"" + message + "\"}").getBytes());
        httpResponse.getOutputStream().flush();
    }
}