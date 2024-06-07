package com.example.payments.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtConfig {

    @Value("${jwt.secretKey}")
    private String secretKey;

}
