package com.hrs.booking.auth;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


// Cache to store User Authentication for faster Login and API calling
@Component
@Getter
public class AuthCacheProvider {
    private final Cache<String, Authentication> authCache = CacheBuilder
            .newBuilder()
            .build();
}
