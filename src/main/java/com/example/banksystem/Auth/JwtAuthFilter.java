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

        // ✅ تجاهل المسارات المفتوحة
        String path = request.getServletPath();
        if (path.equals("/api/register") || path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ استخراج الهيدر
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // ✅ التأكد من وجود Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ إزالة كلمة Bearer
        jwt = authHeader.substring(7);

        // ✅ استخراج اسم المستخدم من التوكن
        username = jwtService.extractUsername(jwt);

        // ✅ التحقق أن المستخدم لم يتم مصادقته بعد
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // ✅ التحقق من صحة التوكن
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // ✅ حفظ التوكن نفسه داخل details عشان نوصله لاحقًا
                authToken.setDetails(jwt);

                // ✅ تخزين المصادقة في SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // ✅ مواصلة السلسلة
        filterChain.doFilter(request, response);
    }
}
