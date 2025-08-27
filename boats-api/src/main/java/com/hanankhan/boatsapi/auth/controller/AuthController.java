package com.hanankhan.boatsapi.auth.controller;

import com.hanankhan.boatsapi.auth.JwtUtil;
import com.hanankhan.boatsapi.auth.dto.JwtResponse;
import com.hanankhan.boatsapi.auth.dto.LoginRequest;
import com.hanankhan.boatsapi.auth.exception.InvalidCredentialsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        try {
            authManager.authenticate(authentication);
        } catch (Exception ex) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        UserDetails user = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());
        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new JwtResponse(token, jwtUtil.getExpirationSeconds()));
    }
}
