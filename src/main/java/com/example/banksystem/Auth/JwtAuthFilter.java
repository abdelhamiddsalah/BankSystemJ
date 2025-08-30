package com.example.banksystem.Auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        if (path.equals("/api/register") ||
                path.equals("/api/login") ||
                path.equals("/forgetpincode") ||
                path.equals("/api/registerAdmin") ||
                path.equals("/api/employee/signup") ||
                path.equals("/api/employee/login") ||
                path.equals("/api/addcopoun") ||
                path.equals("/api/admin/cvs") ||
                path.equals("/api/admin/cv/{id}/update") ||
                path.equals("/api/upload-cv") ||
                path.equals("/api/admin/allEmployers") ||
                path.equals("/api/admin/getEmployer/{id}") ||
                path.equals("/api/admin/employer/{id}") ||
                path.equals("/api/admin/user/{id}") ||
                path.equals("/api/admin/allUsers") ||
                path.startsWith("/auth") ||
                path.startsWith("/webjars") ||
                path.startsWith("/configuration")) {

            filterChain.doFilter(request, response);
            return;
        }


        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(jwt);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
