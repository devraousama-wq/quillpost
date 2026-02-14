package com.quillpost.auth.oauth;

import com.quillpost.content.domain.UserAccount;
import com.quillpost.content.repository.UserAccountRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuillpostOAuth2UserService extends DefaultOAuth2UserService {

    private final UserAccountRepository users;

    public QuillpostOAuth2UserService(UserAccountRepository users) {
        this.users = users;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(userRequest);
        String email = oauthUser.getAttribute("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("email claim missing");
        }
        users.findByEmailIgnoreCase(email).orElseGet(() -> {
            String name = oauthUser.getAttribute("name");
            if (name == null) {
                name = email;
            }
            return users.save(new UserAccount(email.toLowerCase(), name));
        });
        return oauthUser;
    }
}
