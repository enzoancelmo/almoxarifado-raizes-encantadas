package com.estoqueinteligente.auth;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estoqueinteligente.common.BusinessException;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final CustomUserDetailsService userDetails;
    private final String adminSetupToken;

    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository repository,
            PasswordEncoder encoder,
            JwtService jwt,
            CustomUserDetailsService userDetails,
            @Value("${app.admin.setup-token:}") String adminSetupToken) {
        this.authenticationManager = authenticationManager;
        this.repository = repository;
        this.encoder = encoder;
        this.jwt = jwt;
        this.userDetails = userDetails;
        this.adminSetupToken = adminSetupToken;
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getPassword()));
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException("E-mail ou senha inválidos");
        }

        AppUser user = repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AuthenticationFailedException("E-mail ou senha inválidos"));

        if (!user.isActive()) {
            throw new AuthenticationFailedException("Usuário inativo");
        }

        UserDetails details = userDetails.loadUserByUsername(email);
        return new AuthResponse(jwt.generateToken(details), user);
    }

    @Transactional
    public UserResponse resetAdmin(AdminResetRequest request) {
        if (adminSetupToken == null || adminSetupToken.isBlank()) {
            throw new BusinessException("Reset de admin não configurado");
        }

        if (!adminSetupToken.equals(request.getToken())) {
            throw new BusinessException("Token de reset inválido");
        }

        String email = normalizeEmail(request.getEmail());
        AppUser user = repository.findByEmailIgnoreCase(email).orElseGet(AppUser::new);
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(UserRole.ADMIN);
        user.setActive(true);

        return new UserResponse(repository.save(user));
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.getEmail());

        if (repository.existsByEmailIgnoreCase(email)) {
            throw new BusinessException("E-mail já cadastrado");
        }

        AppUser user = new AppUser();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPassword(encoder.encode(request.getPassword()));

        if (request.getRole() != UserRole.USER) {
            throw new BusinessException("O cadastro público permite apenas o perfil USER");
        }

        user.setRole(UserRole.USER);
        user.setActive(true);

        return new UserResponse(repository.save(user));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
