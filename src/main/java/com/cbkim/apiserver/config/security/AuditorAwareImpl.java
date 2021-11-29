package com.cbkim.apiserver.config.security;

import java.util.Optional;

import com.cbkim.apiserver.entity.Users;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

//유저정보를 꺼내올 수 있는 기능
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor()
    {        
        // Spring Security를 통한 Auditor 매핑
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = "";

        if (principal instanceof Users) {
            userId = ((Users)principal).getUsername();
        } else {
            userId = principal.toString();
        }

        return Optional.of(userId);
    }
}

