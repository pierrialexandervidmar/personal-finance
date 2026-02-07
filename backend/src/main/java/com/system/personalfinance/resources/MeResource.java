package com.system.personalfinance.resources;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.system.personalfinance.security.UserPrincipal;

@RestController
public class MeResource {

    @GetMapping("/api/me")
    public Map<String, Object> me(Authentication auth) {
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        return Map.of(
            "userId", principal.getId(),
            "email", principal.getUsername()
        );
    }
}
