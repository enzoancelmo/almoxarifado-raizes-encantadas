package com.estoqueinteligente.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminUserInitializer implements ApplicationRunner {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final String adminName;
    private final String adminEmail;
    private final String adminPassword;
    private final boolean resetAdminPassword;

    public AdminUserInitializer(UserRepository repository, PasswordEncoder encoder,
            @Value("${app.admin.name:Administrador}") String adminName,
            @Value("${app.admin.email:admin@estoque.com}") String adminEmail,
            @Value("${app.admin.password:admin123}") String adminPassword,
            @Value("${app.admin.reset-password:false}") boolean resetAdminPassword) {
        this.repository = repository;
        this.encoder = encoder;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
        this.resetAdminPassword = resetAdminPassword;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        repository.findByEmailIgnoreCase(adminEmail).ifPresentOrElse(existingAdmin -> {
            if (resetAdminPassword) {
                existingAdmin.setName(adminName);
                existingAdmin.setPassword(encoder.encode(adminPassword));
                existingAdmin.setRole(UserRole.ADMIN);
                existingAdmin.setActive(true);
                repository.save(existingAdmin);
            }
        }, () -> {
        AppUser admin = new AppUser();
        admin.setName(adminName);
        admin.setEmail(adminEmail.toLowerCase());
        admin.setPassword(encoder.encode(adminPassword));
        admin.setRole(UserRole.ADMIN);
        admin.setActive(true);
        repository.save(admin);
        });
    }
}
