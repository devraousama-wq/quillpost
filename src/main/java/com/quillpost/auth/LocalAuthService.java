package com.quillpost.auth;

import com.quillpost.content.domain.UserAccount;
import com.quillpost.content.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalAuthService {

    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;

    public LocalAuthService(UserAccountRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserAccount register(String email, String displayName, String rawPassword) {
        users.findByEmailIgnoreCase(email).ifPresent(u -> {
            throw new IllegalArgumentException("email already registered");
        });
        UserAccount account = new UserAccount(email.toLowerCase(), displayName);
        account.setPasswordHash(passwordEncoder.encode(rawPassword));
        return users.save(account);
    }
}
