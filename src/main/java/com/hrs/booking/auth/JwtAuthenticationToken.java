package com.hrs.booking.auth;

import com.hrs.booking.entity.CurrentUser;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

// A Wrapper on Spring Security Authentication class.
public class JwtAuthenticationToken extends AbstractAuthenticationToken
{

    //user object
    private final Object principal;

    //jwt token
    private final Object credentials;

    @Getter
    private boolean stale;

    public JwtAuthenticationToken(Object principal, Object credentials,
                                  Collection<? extends GrantedAuthority> authorities)
    {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials()
    {
        return credentials;
    }

    @Override
    public Object getPrincipal()
    {
        return principal;
    }

}