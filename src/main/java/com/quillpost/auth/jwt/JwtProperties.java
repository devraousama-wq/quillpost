package com.quillpost.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "quillpost.jwt")
public record JwtProperties(String secret) {
}
