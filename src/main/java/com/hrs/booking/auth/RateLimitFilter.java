package com.hrs.booking.auth;

import com.google.common.util.concurrent.RateLimiter;
import com.hrs.booking.entity.CurrentUser;
import com.hrs.booking.entity.HRSUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RateLimitFilter extends GenericFilterBean
{

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Value("${hrs.auth.apiprefix}")
    private String apiprefix;

    @Value("${hrs.auth.persec_limit}")
    private int perseclimit;

    private RateLimiter limit;

    @PostConstruct
    public void init() {
        limit = RateLimiter.create(perseclimit);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {

        HttpServletRequest req = (HttpServletRequest) request;
        if (!req.getRequestURI().startsWith(apiprefix))
        {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
        {
            throw new RuntimeException("Deny: auth missing");
        }

        if (authentication.getPrincipal() instanceof CurrentUser)
        {
            HRSUser user = ((CurrentUser) authentication.getPrincipal()).getUser();

            if (!limit.tryAcquire(1, TimeUnit.SECONDS))
            {
                logger.warn("Who is trying to mug us? {} ", user.getEmail());
                throw new RuntimeException("Deny: api rate exceeded -" + limit.getRate());
            }

        }
        else
        {
            throw new RuntimeException("Deny: auth missing");
        }
        chain.doFilter(request, response);
    }

}