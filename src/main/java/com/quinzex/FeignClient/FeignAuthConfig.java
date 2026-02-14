package com.quinzex.FeignClient;

import com.quinzex.entity.LmsLogin;
import com.quinzex.entity.Permission;
import com.quinzex.repository.LmsLoginRepo;
import com.quinzex.service.JwtService;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class FeignAuthConfig {

    private final JwtService jwtService;
    private final LmsLoginRepo lmsLoginRepo;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {

            var authentication = SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return;
            }

            // 1️⃣ Logged-in user email
            String email = authentication.getName();

            // 2️⃣ Fetch user
            LmsLogin user = lmsLoginRepo.findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("User not found: " + email)
                    );

            // 3️⃣ Role
            Set<String> roles = Set.of(
                    user.getRole().getRoleName()
            );

            // 4️⃣ Permissions (THIS WAS THE BUG)
            Set<String> permissions = user.getRole()
                    .getPermissions()
                    .stream()
                    .map(Permission::getName) // ✅ correct
                    .collect(Collectors.toSet());

            // 5️⃣ Generate USER JWT (RSA)
            String userJwt = jwtService.generateAccessToken(
                    user.getEmail(),
                    roles,
                    permissions,
                    user.getTokenVersion(),
                    user.getRole().getRoleVersion()
            );

            // 6️⃣ Forward to Django
            requestTemplate.header(
                    "Authorization",
                    "Bearer " + userJwt
            );
        };
    }
}
