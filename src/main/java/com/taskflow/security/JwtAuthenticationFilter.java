package com.taskflow.security;

import com.taskflow.model.entity.User;
import com.taskflow.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
// OncePerRequestFilter — guaranteed to run exactly once per request
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // ── Step 1: Extract the Authorization header ──────────
        String authHeader = request.getHeader("Authorization");

        // If no header or doesn't start with "Bearer " — skip filter
        // This request is either public or will be rejected by security rules
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ── Step 2: Extract the token ─────────────────────────
        // "Bearer eyJhbGci..." → "eyJhbGci..."
        String token = authHeader.substring(7);

        // ── Step 3: Validate the token ────────────────────────
        if (!jwtUtil.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // ── Step 4: Extract identity from token ───────────────
        String email = jwtUtil.extractEmail(token);
        String role  = jwtUtil.extractRole(token);

        // ── Step 5: Load user from database ───────────────────
        // Only proceed if not already authenticated in this request
        if (email != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null) {
                // ── Step 6: Build Spring Security authentication ──
                // "ROLE_" prefix is required by Spring Security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role))
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // ── Step 7: Tell Spring Security this request
                //            is authenticated ──────────────────
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        // ── Step 8: Continue to the next filter / controller ──
        filterChain.doFilter(request, response);
    }
}
