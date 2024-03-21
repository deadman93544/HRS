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
    private String apiprefix;                          // A Prefix which is used as starting endpoint of all Authorized APIs

    @Value("${hrs.auth.persec_limit}")
    private int perseclimit;                           // Persec limit on the APIs (customizable) to prevent DOS Attack.

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
//            The URI does not contains the Prefix for Authorized Endpoint, Passing them.
            chain.doFilter(request, response);
            return;
        }

//        Fetching the Authentication from Spring Security Context set by previous layers.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
        {
            throw new RuntimeException("Deny: auth missing");
        }

        if (authentication.getPrincipal() instanceof CurrentUser)
        {
            HRSUser user = ((CurrentUser) authentication.getPrincipal()).getUser();

//            Checking the API Rate limit.
//            This can be helpful in creating an alert to the stakeholders to level up the resources.
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