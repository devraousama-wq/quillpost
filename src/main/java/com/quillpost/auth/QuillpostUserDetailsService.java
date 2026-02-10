package com.quillpost.auth;

import com.quillpost.content.domain.UserAccount;
import com.quillpost.content.repository.UserAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class QuillpostUserDetailsService implements UserDetailsService {

    private final UserAccountRepository users;

    public QuillpostUserDetailsService(UserAccountRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserAccount account = users.findByEmailIgnoreCase(username)
            .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        if (account.getPasswordHash() == null) {
            throw new UsernameNotFoundException("password login unavailable");
        }
        return User.withUsername(account.getEmail())
            .password(account.getPasswordHash())
            .roles("USER")
            .build();
    }
}
